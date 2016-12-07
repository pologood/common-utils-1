/**
 * 
 */
package com.baidu.unbiz.common.codec;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.baidu.unbiz.common.CharUtil;
import com.baidu.unbiz.common.StringPool;

/**
 * URL编码
 * <p>
 * 如: {@literal https://xuchen06:xxx@www.baidu.com:8080/file;p=1?q=2#third}:
 * <ul>
 * <li>scheme (https)</li>
 * <li>user (xuchen06)</li>
 * <li>password (xxx)</li>
 * <li>host (www.baidu.com)</li>
 * <li>port (8080)</li>
 * <li>path (file)</li>
 * <li>path parameter (p=1)</li>
 * <li>query parameter (q=2)</li>
 * <li>fragment (third)</li>
 * </ul>
 * 
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015年11月18日 上午3:35:31
 */
public abstract class URLEncoder implements StringPool {

    private static final String SCHEME_PATTERN = "([^:/?#]+):";

    private static final String HTTP_PATTERN = "(http|https):";

    private static final String USERINFO_PATTERN = "([^@/]*)";

    private static final String HOST_PATTERN = "([^/?#:]*)";

    private static final String PORT_PATTERN = "(\\d*)";

    private static final String PATH_PATTERN = "([^?#]*)";

    private static final String QUERY_PATTERN = "([^#]*)";

    private static final String LAST_PATTERN = "(.*)";

    // See RFC 3986, appendix B

    private static final Pattern URI_PATTERN = Pattern.compile(
            "^(" + SCHEME_PATTERN + ")?" + "(//(" + USERINFO_PATTERN + "@)?" + HOST_PATTERN + "(:" + PORT_PATTERN + ")?"
                    + ")?" + PATH_PATTERN + "(\\?" + QUERY_PATTERN + ")?" + "(#" + LAST_PATTERN + ")?");

    private static final Pattern HTTP_URL_PATTERN = Pattern.compile('^' + HTTP_PATTERN + "(//(" + USERINFO_PATTERN
            + "@)?" + HOST_PATTERN + "(:" + PORT_PATTERN + ")?" + ")?" + PATH_PATTERN + "(\\?" + LAST_PATTERN + ")?");

    /**
     * URI组件，@see <a href="http://www.ietf.org/rfc/rfc3986.txt">RFC 3986</a>
     * 
     * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
     * @version create on 2015年11月19日 上午1:52:01
     */
    enum URIPart {

        SCHEME {
            @Override
            public boolean isValid(char ch) {
                return CharUtil.isAlpha(ch) || CharUtil.isDigit(ch) || ch == '+' || ch == '-' || ch == '.';
            }
        },
        AUTHORITY {
            @Override
            public boolean isValid(char ch) {
                return CharUtil.isUnreserved(ch) || CharUtil.isSubDelimiter(ch) || ch == ':' || ch == '@';
            }
        },
        USER_INFO {
            @Override
            public boolean isValid(char ch) {
                return CharUtil.isUnreserved(ch) || CharUtil.isSubDelimiter(ch) || ch == ':';
            }
        },
        HOST {
            @Override
            public boolean isValid(char ch) {
                return CharUtil.isUnreserved(ch) || CharUtil.isSubDelimiter(ch);
            }
        },
        PORT {
            @Override
            public boolean isValid(char ch) {
                return CharUtil.isDigit(ch);
            }
        },
        PATH {
            @Override
            public boolean isValid(char ch) {
                return CharUtil.isPchar(ch) || ch == '/';
            }
        },
        PATH_SEGMENT {
            @Override
            public boolean isValid(char ch) {
                return CharUtil.isPchar(ch);
            }
        },
        QUERY {
            @Override
            public boolean isValid(char ch) {
                return CharUtil.isPchar(ch) || ch == '/' || ch == '?';
            }
        },
        QUERY_PARAM {
            @Override
            public boolean isValid(char ch) {
                if (ch == '=' || ch == '+' || ch == '&' || ch == ';') {
                    return false;
                }
                return CharUtil.isPchar(ch) || ch == '/' || ch == '?';
            }
        },
        FRAGMENT {
            @Override
            public boolean isValid(char ch) {
                return CharUtil.isPchar(ch) || ch == '/' || ch == '?';
            }
        };

        public abstract boolean isValid(char ch);

    }

    private static String encodeUriComponent(String source, String encoding, URIPart uriPart) {
        if (source == null) {
            return null;
        }

        byte[] bytes;
        try {
            bytes = encodeBytes(source.getBytes(encoding), uriPart);
        } catch (UnsupportedEncodingException ignore) {
            return null;
        }

        char[] chars = new char[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            chars[i] = (char) bytes[i];
        }
        return new String(chars);
    }

    private static byte[] encodeBytes(byte[] source, URIPart uriPart) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(source.length);
        for (byte b : source) {
            if (b < 0) {
                b += 256;
            }
            if (uriPart.isValid((char) b)) {
                bos.write(b);
                continue;
            }

            bos.write('%');
            char hex1 = Character.toUpperCase(Character.forDigit((b >> 4) & 0xF, 16));
            char hex2 = Character.toUpperCase(Character.forDigit(b & 0xF, 16));
            bos.write(hex1);
            bos.write(hex2);
        }
        return bos.toByteArray();
    }

    public static String encodeScheme(String scheme, String encoding) {
        return encodeUriComponent(scheme, encoding, URIPart.SCHEME);
    }

    public static String encodeScheme(String scheme) {
        return encodeUriComponent(scheme, Charset.UTF_8, URIPart.SCHEME);
    }

    public static String encodeUserInfo(String userInfo, String encoding) {
        return encodeUriComponent(userInfo, encoding, URIPart.USER_INFO);
    }

    public static String encodeUserInfo(String userInfo) {
        return encodeUriComponent(userInfo, Charset.UTF_8, URIPart.USER_INFO);
    }

    public static String encodeHost(String host, String encoding) {
        return encodeUriComponent(host, encoding, URIPart.HOST);
    }

    public static String encodeHost(String host) {
        return encodeUriComponent(host, Charset.UTF_8, URIPart.HOST);
    }

    public static String encodePort(String port, String encoding) {
        return encodeUriComponent(port, encoding, URIPart.PORT);
    }

    public static String encodePort(String port) {
        return encodeUriComponent(port, Charset.UTF_8, URIPart.PORT);
    }

    public static String encodePath(String path, String encoding) {
        return encodeUriComponent(path, encoding, URIPart.PATH);
    }

    public static String encodePath(String path) {
        return encodeUriComponent(path, Charset.UTF_8, URIPart.PATH);
    }

    public static String encodePathSegment(String segment, String encoding) {
        return encodeUriComponent(segment, encoding, URIPart.PATH_SEGMENT);
    }

    public static String encodePathSegment(String segment) {
        return encodeUriComponent(segment, Charset.UTF_8, URIPart.PATH_SEGMENT);
    }

    public static String encodeQuery(String query, String encoding) {
        return encodeUriComponent(query, encoding, URIPart.QUERY);
    }

    public static String encodeQuery(String query) {
        return encodeUriComponent(query, Charset.UTF_8, URIPart.QUERY);
    }

    public static String encodeQueryParam(String queryParam, String encoding) {
        return encodeUriComponent(queryParam, encoding, URIPart.QUERY_PARAM);
    }

    public static String encodeQueryParam(String queryParam) {
        return encodeUriComponent(queryParam, Charset.UTF_8, URIPart.QUERY_PARAM);
    }

    public static String encodeFragment(String fragment, String encoding) {
        return encodeUriComponent(fragment, encoding, URIPart.FRAGMENT);
    }

    public static String encodeFragment(String fragment) {
        return encodeUriComponent(fragment, Charset.UTF_8, URIPart.FRAGMENT);
    }

    public static String encodeUri(String uri) {
        return encodeUri(uri, Charset.UTF_8);
    }

    public static String encodeUri(String uri, String encoding) {
        Matcher m = URI_PATTERN.matcher(uri);
        if (m.matches()) {
            String scheme = m.group(2);
            String authority = m.group(3);
            String userinfo = m.group(5);
            String host = m.group(6);
            String port = m.group(8);
            String path = m.group(9);
            String query = m.group(11);
            String fragment = m.group(13);

            return encodeUriComponents(scheme, authority, userinfo, host, port, path, query, fragment, encoding);
        }
        throw new IllegalArgumentException("Invalid URI: " + uri);
    }

    public static String encodeHttpUrl(String httpUrl) {
        return encodeHttpUrl(httpUrl, Charset.UTF_8);
    }

    public static String encodeHttpUrl(String httpUrl, String encoding) {
        Matcher m = HTTP_URL_PATTERN.matcher(httpUrl);
        if (m.matches()) {
            String scheme = m.group(1);
            String authority = m.group(2);
            String userinfo = m.group(4);
            String host = m.group(5);
            String portString = m.group(7);
            String path = m.group(8);
            String query = m.group(10);

            return encodeUriComponents(scheme, authority, userinfo, host, portString, path, query, null, encoding);
        }
        throw new IllegalArgumentException("Invalid HTTP URL: " + httpUrl);
    }

    private static String encodeUriComponents(String scheme, String authority, String userInfo, String host,
            String port, String path, String query, String fragment, String encoding) {

        StringBuilder sb = new StringBuilder();

        if (scheme != null) {
            sb.append(encodeScheme(scheme, encoding));
            sb.append(':');
        }

        if (authority != null) {
            sb.append("//");
            if (userInfo != null) {
                sb.append(encodeUserInfo(userInfo, encoding));
                sb.append('@');
            }
            if (host != null) {
                sb.append(encodeHost(host, encoding));
            }
            if (port != null) {
                sb.append(':');
                sb.append(encodePort(port, encoding));
            }
        }

        sb.append(encodePath(path, encoding));

        if (query != null) {
            sb.append('?');
            sb.append(encodeQuery(query, encoding));
        }

        if (fragment != null) {
            sb.append('#');
            sb.append(encodeFragment(fragment, encoding));
        }

        return sb.toString();
    }

    public static Builder build(String path) {
        return build(path, true);
    }

    public static Builder build(String path, boolean encodePath) {
        return new Builder(path, encodePath, Charset.UTF_8);
    }

    public static class Builder {
        protected final StringBuilder url;
        protected final String encoding;
        protected boolean hasParams;

        public Builder(String path, boolean encodePath, String encoding) {
            this.encoding = encoding;
            url = new StringBuilder();
            if (encodePath) {
                url.append(encodeUri(path, encoding));
            } else {
                url.append(path);
            }
            this.hasParams = url.indexOf(StringPool.Symbol.QUESTION_MARK) != -1;
        }

        public Builder queryParam(String name, String value) {
            url.append(hasParams ? '&' : '?');
            hasParams = true;

            url.append(encodeQueryParam(name, encoding));

            if ((value != null) && (value.length() > 0)) {
                url.append('=');
                url.append(encodeQueryParam(value, encoding));
            }
            return this;
        }

        @Override
        public String toString() {
            return url.toString();
        }
    }

}