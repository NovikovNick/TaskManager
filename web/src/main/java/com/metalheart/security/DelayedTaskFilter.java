package com.metalheart.security;

import com.metalheart.config.AppProperties;
import com.metalheart.model.DelayedTask;
import com.metalheart.model.User;
import com.metalheart.model.request.ChangePasswordDelayedTask;
import com.metalheart.model.request.UserRegistrationDelayedTask;
import com.metalheart.service.DelayedTaskService;
import com.metalheart.service.RegistrationService;
import com.metalheart.service.UserService;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.UrlPathHelper;

@Slf4j
@Component
public class DelayedTaskFilter extends OncePerRequestFilter {

    public static final String UUID_REGEX = "[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}";

    @Autowired
    private AppProperties properties;

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private DelayedTaskService tokenService;

    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        String path = new UrlPathHelper().getPathWithinApplication(request);
        if (path.length() == 37 && path.substring(1).matches(UUID_REGEX)) {

            try {

                String registrationToken = path.substring(1);
                UUID taskId = UUID.fromString(registrationToken);

                DelayedTask task = tokenService.get(taskId);

                if (Objects.isNull(task)) {
                    response.sendRedirect(properties.getRest().getFrontUrl() + "/expired/token");
                    return;
                }

                if (task instanceof UserRegistrationDelayedTask) {

                    Optional<User> user = registrationService.confirmRegistration(taskId);

                    if (user.isPresent()) {

                        authenticate(request, user.get());
                        response.sendRedirect(properties.getRest().getFrontUrl());
                        return;
                    }

                } else if (task instanceof ChangePasswordDelayedTask) {

                    ChangePasswordDelayedTask changePasswordDelayedTask = (ChangePasswordDelayedTask) task;
                    Optional<User> user = userService.findByEmail(changePasswordDelayedTask.getEmail());

                    if (user.isPresent()) {

                        authenticate(request, user.get());
                        response.sendRedirect(properties.getRest().getFrontUrl() + "/changepassword");
                        return;
                    }
                }

            } catch (Exception failed) {
                log.error(failed.getMessage(), failed);
                SecurityContextHolder.clearContext();
                chain.doFilter(request, response);
            }

        }

        chain.doFilter(request, response);
    }

    private void authenticate(HttpServletRequest request, User principal) {
        Token token = new Token(principal);
        token.setAuthenticated(true);
        SecurityContextHolder.getContext().setAuthentication(token);

        final HttpSession session = request.getSession(true);
        session.setAttribute(
            HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
            SecurityContextHolder.getContext());
    }
}
