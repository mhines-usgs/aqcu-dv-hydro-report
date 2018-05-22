package gov.usgs.aqcu.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import feign.RequestInterceptor;
import feign.RequestTemplate;

@Component
public class CidaAuthRequestInterceptor implements RequestInterceptor {

	@Override
	public void apply(RequestTemplate template) {
		if(SecurityContextHolder.getContext().getAuthentication() instanceof CidaAuthAuthenticationToken) {
			CidaAuthAuthenticationToken auth = (CidaAuthAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
			template.header(CidaAuthTokenSecurityFilter.AUTHORIZATION_HEADER, CidaAuthTokenSecurityFilter.AUTH_BEARER_STRING + auth.getToken());
		}
	}
}
