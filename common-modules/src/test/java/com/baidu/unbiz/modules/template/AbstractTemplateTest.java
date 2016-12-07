package com.baidu.unbiz.modules.template;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;

import com.baidu.unbiz.common.FileUtil;
import com.baidu.unbiz.common.io.StreamUtil;
import com.baidu.unbiz.modules.template.Template.Location;

/**
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015-2-5 上午10:24:50
 */
public abstract class AbstractTemplateTest {
    protected final File srcdir = new File("src/test/config/", "templates");
    protected String source;
    protected Template template;
    protected TemplateParseException parseError;
    protected TemplateRuntimeException runtimeError;

    protected void loadTemplateFailure(byte[] content, String systemId) {
        try {
            loadTemplate(content, systemId, -1, -1, -1);
            fail();
        } catch (TemplateParseException e) {
            this.parseError = e;
        }
    }

    protected void loadTemplate(String file, int nodesCount, int templatesCount, int paramsCount) {
        source = file;
        template = new Template(new File(srcdir, file));

        assertTemplate(template, null, nodesCount, templatesCount, paramsCount, null);
    }

    protected void loadTemplate(byte[] content, String systemId, int nodesCount, int templatesCount, int paramsCount) {
        source = systemId;
        template = new Template(new ByteArrayInputStream(content), systemId);

        assertTemplate(template, null, nodesCount, templatesCount, paramsCount, null);
    }

    protected void assertTemplate(Template template, String name, int nodesCount, int templatesCount, int paramsCount,
            String location) {
        assertEquals(name, template.getName());
        assertLocation(template.location, location);

        String str = template.toString();

        if (name == null) {
            name = "(template)";
        }

        assertThat(str, startsWith("#" + name + " with " + nodesCount + " nodes at "));
        assertLocation(str, location);

        assertEquals(nodesCount, template.nodes.length);
        assertEquals(templatesCount, template.subtemplates.size());
        assertEquals(paramsCount, template.params.size());
    }

    protected void assertLocation(String str, String location) {
        if (location != null) {
            assertThat(str, containsString(source + ": " + location));
        } else {
            assertThat(str, containsString(source));
        }
    }

    protected void assertLocation(Location l, String location) {
        if (location != null) {
            assertThat(l.toString(), endsWith(source + ": " + location));
        } else {
            assertThat(l.toString(), endsWith(source));
        }
    }

    protected URL copyFile(String src, String dest) throws IOException {
        File destfile = new File("target/test", dest);

        StreamUtil.io(new FileInputStream(new File(srcdir, src)), new FileOutputStream(destfile), true, true);

        return destfile.toURI().toURL();
    }

    protected URL copyFilesToJar(String destJar, String...srcdest) throws IOException {
        assertTrue(srcdest.length % 2 == 0);
        FileUtil.createDir("target/test");
        File destJarFile = new File("target/test", destJar);
        JarOutputStream jos = new JarOutputStream(new FileOutputStream(destJarFile));

        for (int i = 0; i < srcdest.length; i += 2) {
            String src = srcdest[i];
            String dest = srcdest[i + 1];

            jos.putNextEntry(new ZipEntry(dest));
            StreamUtil.io(new FileInputStream(new File(srcdir, src)), jos, true, false);
            jos.closeEntry();
        }

        jos.close();

        return destJarFile.toURI().toURL();
    }

    protected void acceptFailure(Object visitor) {
        try {
            template.accept(visitor);
            fail();
        } catch (TemplateRuntimeException e) {
            runtimeError = e;
        }
    }
}
