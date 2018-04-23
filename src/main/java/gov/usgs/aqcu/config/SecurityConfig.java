package gov.usgs.aqcu.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import gov.usgs.aqcu.security.CidaAuthTokenSecurityFilter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private CidaAuthTokenSecurityFilter cidaAuthTokenSecurityFilter;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.httpBasic().disable()
			.cors().and()
			.authorizeRequests()
				.anyRequest().permitAll()
			.and()
				.logout().permitAll()
			.and()
				.csrf().disable()
				.addFilterBefore(cidaAuthTokenSecurityFilter, UsernamePasswordAuthenticationFilter.class)
		;
	}

}
