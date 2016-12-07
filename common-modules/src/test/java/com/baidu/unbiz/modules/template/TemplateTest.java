package com.baidu.unbiz.modules.template;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import com.baidu.unbiz.common.test.TestUtil;

/**
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015-2-10 上午10:26:46
 */
public class TemplateTest extends AbstractTemplateTest {
    @Before
    public void init() {
        template = new Template(new File(srcdir, "test06_real_case.txt"));
    }

    @Test
    public void getSubTemplate() throws Exception {
        assertEquals("itemlist", template.getSubTemplate("itemlist").getName());
        assertEquals("dateItem", template.getSubTemplate("itemlist").getSubTemplate("dateItem").getName());
        assertEquals("datetimeItem", template.getSubTemplate("itemlist").getSubTemplate("datetimeItem").getName());
    }

    @Test
    public void getParameter() throws Exception {
        assertEquals("UTF-8", template.getParameter("charset"));
        assertEquals("on", template.getParameter("trimming"));
        assertEquals("collapse", template.getParameter("whitespace"));

        assertEquals(null, template.getParameter("notexist"));
    }

    @Test
    public void toString_() throws Exception {
        String s = template.toString();

        assertThat(s, TestUtil.containsAllRegex("#\\(template\\) with 5 nodes at .+test06_real_case.txt \\{\n" //
                , "params\\s+= \\[" //
                , "nodes\\s+= \\[" //
                , "sub-templates\\s+= \\[" //
                , "\\[1/3\\] charset=UTF-8" //
                , "\\[1/5\\] Text with 21 characters:" //
                , "\\[2/5\\] \\$\\{title:我的标题\\} at .+test06_real_case.txt: Line 7 Column 12" //
                , "\\[1/1\\] #itemlist with 3 nodes at .+test06_real_case.txt: Line 14 Column 1" //
        ));
    }
}
