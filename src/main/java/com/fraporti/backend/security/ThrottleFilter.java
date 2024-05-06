package com.fraporti.backend.security;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class ThrottleFilter extends OncePerRequestFilter {
    private static final int MAX_ATTEMPTS = 100;
    private static final long LOCK_TIME = 60 * 1000;// 1 minute

    private static final Map<String, Map<String, Integer>> attempt = new HashMap<>();
    private static final Map<String, Long> locked = new HashMap<>();

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        String ip = getClientIP(request);
        String url = request.getRequestURI();
        if(locked.containsKey(ip)) {
            long time = locked.get(ip);
            if(System.currentTimeMillis() - time < LOCK_TIME) {
                response.sendError(429);
                return;
            }
            locked.remove(ip);
            attempt.get(ip).put(url, 0);
        }
        Map<String, Integer> attempts = attempt.get(ip);
        if (attempts == null) {
            attempt.put(ip, new HashMap<>());
            attempt.get(ip).put(url, 1);
        } else {
            Integer tetnativas = attempts.get(url);
            if (tetnativas != null) {
                if (tetnativas >= MAX_ATTEMPTS) {
                    locked.put(ip, System.currentTimeMillis());
                    response.sendError(429);
                    return;
                }
                attempts.put(url, tetnativas + 1);
            } else {
                attempts.put(url, 1);
            }
        }
        filterChain.doFilter(request, response);
    }

    private String getClientIP(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null || xfHeader.isEmpty() || !xfHeader.contains(request.getRemoteAddr())) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}
