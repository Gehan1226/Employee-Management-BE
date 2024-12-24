package edu.icet.demo.aspect;

import edu.icet.demo.aspect.anotations.HasEndpointAuthorities;
import edu.icet.demo.exception.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Aspect
@Component
@Slf4j
public class HasEndpointAuthoritiesAspect {

    @Before("within(@org.springframework.web.bind.annotation.RestController *) && @annotation(authorities)")
    public void hasAuthorities(final JoinPoint joinPoint, final HasEndpointAuthorities authorities) {
        final SecurityContext securityContext = SecurityContextHolder.getContext();

        if (!Objects.isNull(securityContext)) {
            final Authentication authentication = securityContext.getAuthentication();
            if (!Objects.isNull(authentication)) {
                final String username = authentication.getName();
                final Collection<? extends GrantedAuthority> userAuthorities = authentication.getAuthorities();

                List<String> userAuthorityNames = userAuthorities.stream()
                        .map(GrantedAuthority::getAuthority).toList();

                boolean hasRequiredAuthority = Stream.of(authorities.authorities())
                        .anyMatch(requiredAuthority ->
                                userAuthorityNames.contains(requiredAuthority.name())
                        );

                if (!hasRequiredAuthority) {
                    throw new UnauthorizedException(
                            "User %s does not have the correct authorities required by endpoint"
                                    .formatted(username)
                    );
                }
            } else {
                throw new UnauthorizedException("Access denied: You must be logged in to perform this action.");
            }
        } else {
            throw new UnauthorizedException(
                    "Access denied: Unable to verify your access. Please log in and try again."
            );
        }
    }
}
