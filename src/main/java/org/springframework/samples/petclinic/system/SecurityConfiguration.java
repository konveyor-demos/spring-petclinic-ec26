package org.springframework.samples.petclinic.system;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.context.annotation.Bean;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/", "/oups").permitAll()
				.antMatchers("/owners/find", "/owners", "/vets.html", "/vets").permitAll()
				.antMatchers("/owners/new", "/owners/*/edit").hasRole("USER")
				.antMatchers("/owners/*/pets/new", "/owners/*/pets/*/edit").hasRole("USER")
				.antMatchers("/owners/*/pets/*/visits/new").hasRole("USER").anyRequest().authenticated().and()
				.httpBasic().and().csrf().disable();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().withUser("admin").password("admin123").roles("ADMIN").and().withUser("user")
				.password("user123").roles("USER");
	}

	@Bean
	@SuppressWarnings("deprecation")
	public static NoOpPasswordEncoder passwordEncoder() {
		return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
	}

}
