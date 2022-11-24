package com.mindhub.homebanking.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@EnableWebSecurity
@Configuration
public class WebAuthorization extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()

                .antMatchers("/h2-console/**", "/rest/**").hasAuthority("ADMIN")

                .antMatchers(HttpMethod.POST, "/api/admin/loans").hasAuthority("ADMIN")

                .antMatchers(HttpMethod.POST, "/api/**").permitAll()

                .antMatchers("/web/js/index.js", "/web/index.html", "/web/assets/**", "/web/usuario-financiero.html", "/web/cuenta-gratuita.html", "/web/peps.html", "/web/facta.html").permitAll()

                .antMatchers("/web/**").hasAnyAuthority("CLIENT", "ADMIN");



        http.formLogin()

                .usernameParameter("email")

                .passwordParameter("password")

                .loginPage("/api/login");



        http.logout().logoutUrl("/api/logout").deleteCookies("JSESSIONID");

        // turn off checking for CSRF tokens
        http.csrf().disable();

        //disabling frameOptions so h2-console can be accessed
        //desactiva frameoptions para poder acceder a la consola del h2
        http.headers().frameOptions().disable();

        // if user is not authenticated, just send an authentication failure response
        //si el usuario no está autenticado, responde con la siguiente respuesta
        http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if login is successful, just clear the flags asking for authentication
        //si el ingresos es existoso, limpia las preguntas de autenticacion
        http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

        // if login fails, just send an authentication failure response
        //si el ingreso falla, responde con la siguiente respuesta
        http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if logout is successful, just send a success response
        //si el logout es exitoso, te responde que no tuviste ningun problema
        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());

    }

    private void clearAuthenticationAttributes(HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        if (session != null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
    }

}
