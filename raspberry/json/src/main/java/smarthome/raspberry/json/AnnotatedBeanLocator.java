package smarthome.raspberry.json;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.type.StandardMethodMetadata;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;

public class AnnotatedBeanLocator {

    private final ConfigurableApplicationContext applicationContext;

    public AnnotatedBeanLocator(ApplicationContext applicationContext) {

        Preconditions.checkNotNull(applicationContext, "applicationContext is null");
        Preconditions.checkArgument(applicationContext instanceof ConfigurableApplicationContext, "Expected ConfigurableApplicationContext but was %s", applicationContext.getClass());

        this.applicationContext = (ConfigurableApplicationContext) applicationContext;
    }

    public List<String> getBeansWithAnnotation(Class<? extends Annotation> type) {
        Predicate<Map<String, Object>> filter = Predicates.alwaysTrue();
        return getBeansWithAnnotation(type, filter);
    }

    public List<String> getBeansWithAnnotation(Class<? extends Annotation> type, Predicate<Map<String, Object>> attributeFilter) {

        List<String> result = Lists.newArrayList();

        ConfigurableListableBeanFactory factory = applicationContext.getBeanFactory();
        for (String name : factory.getBeanDefinitionNames()) {
            BeanDefinition bd = factory.getBeanDefinition(name);

            if (bd.getSource() instanceof StandardMethodMetadata) {
                StandardMethodMetadata metadata = (StandardMethodMetadata) bd.getSource();

                Map<String, Object> attributes = metadata.getAnnotationAttributes(type.getName());
                if (null == attributes) {
                    continue;
                }

                if (attributeFilter.apply(attributes)) {
                    result.add(name);
                }
            }
        }

        return result;
    }
}
