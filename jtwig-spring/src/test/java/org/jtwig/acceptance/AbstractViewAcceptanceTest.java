package org.jtwig.acceptance;

import org.jtwig.mvc.JtwigViewResolver;
import org.jtwig.resource.JtwigResource;
import org.jtwig.resource.StringJtwigResource;
import org.jtwig.resource.loader.JtwigResourceResolver;
import org.junit.Before;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.theme.FixedThemeResolver;

import java.util.Locale;

public abstract class AbstractViewAcceptanceTest {
    private JtwigViewResolver viewResolver;
    private AnnotationConfigWebApplicationContext applicationContext;
    private MockHttpServletRequest httpServletRequest;

    @Before
    public void setUp() throws Exception {
        MockServletContext servletContext = new MockServletContext();
        httpServletRequest = new MockHttpServletRequest();
        viewResolver = new JtwigViewResolver();

        applicationContext = new AnnotationConfigWebApplicationContext();
        applicationContext.setServletContext(servletContext);
        servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, applicationContext);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(httpServletRequest));

        applicationContext.refresh();
        applicationContext.getBeanFactory().registerSingleton("viewResolver", viewResolver);

        viewResolver.setApplicationContext(applicationContext);
    }

    protected void registerBean (String name, Object instance) {
        applicationContext.getAutowireCapableBeanFactory().autowireBean(instance);
        applicationContext.getBeanFactory().registerSingleton(name, instance);
    }

    protected void withDefaultThemeResolver () {
        FixedThemeResolver themeResolver = new FixedThemeResolver();
        themeResolver.setDefaultThemeName("default");
        registerBean("themeResolver", themeResolver);
        viewResolver.setThemeResolver(themeResolver);
    }

    public String render (String location, ModelMap modelMap) throws Exception {
        MockHttpServletResponse response = new MockHttpServletResponse();
        viewResolver.resolveViewName(location, Locale.getDefault())
                .render(modelMap, httpServletRequest, response);

        return response.getContentAsString();
    }

    public String renderString (final String inlineTemplate, ModelMap modelMap) throws Exception {
        MockHttpServletResponse response = new MockHttpServletResponse();
        viewResolver.setResourceLoader(new JtwigResourceResolver() {
            @Override
            public JtwigResource resolve(String viewUrl) {
                return new StringJtwigResource(inlineTemplate);
            }
        });
        viewResolver.resolveViewName("", Locale.getDefault())
                .render(modelMap, new MockHttpServletRequest(), response);

        return response.getContentAsString();
    }

    public String renderString (final String inlineTemplate) throws Exception {
        return renderString(inlineTemplate, new ModelMap());
    }

    protected JtwigViewResolver viewResolver() {
        return viewResolver;
    }
}
