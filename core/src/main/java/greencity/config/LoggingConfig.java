package greencity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@Configuration
public class LoggingConfig {

    @Bean
    public CommonsRequestLoggingFilter requestLoggingFilter() {
        CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();
        filter.setIncludeQueryString(true);        // Логировать параметры запроса
        filter.setIncludePayload(true);           // Логировать тело запроса
        filter.setIncludeHeaders(false);          // Логировать заголовки запроса (опционально)
        filter.setIncludeClientInfo(true);        // Логировать IP-адрес клиента
        filter.setMaxPayloadLength(10000);        // Ограничение на размер тела запроса в логах
        return filter;
    }
}