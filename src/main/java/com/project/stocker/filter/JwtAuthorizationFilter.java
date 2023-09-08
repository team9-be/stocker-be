package com.project.stocker.filter;

import com.project.stocker.entity.UserRoleEnum;
import com.project.stocker.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j(topic = "JWT 검증 및 인가")
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final RedisTemplate<String, String> redisTemplate;

    public JwtAuthorizationFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService, RedisTemplate<String, String> redisTemplate) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.redisTemplate=redisTemplate;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {

        String tokenValue = jwtUtil.getJwtFromHeader(req);
        if (StringUtils.hasText(tokenValue)) {
            if (!jwtUtil.validateToken(tokenValue)) {
                String refreshTokenFromHeader = jwtUtil.getRefreshTokenFromHeader(req);
                String userEmail = jwtUtil.getUserEmailFromToken(refreshTokenFromHeader);
                String tokenFromRedis = redisTemplate.opsForValue().get(userEmail);
                tokenFromRedis = tokenFromRedis.substring(7);

                if (StringUtils.hasText(tokenFromRedis) && jwtUtil.validateToken(tokenFromRedis)) {
                    String username = jwtUtil.getUserInfoFromToken(tokenFromRedis).getSubject();
                    String newAccessToken = jwtUtil.createToken(username, UserRoleEnum.USER);
                    newAccessToken=newAccessToken.substring(7);
                    res.setHeader("Authorization", newAccessToken);
                    Claims info = jwtUtil.getUserInfoFromToken(newAccessToken);
                    setAuthentication(info.getSubject());
                } else {
                    log.error("Invalid Refresh Token");
                    return;
                }
            } else {
                // 액세스 토큰이 유효한 경우
                Claims info = jwtUtil.getUserInfoFromToken(tokenValue);
                setAuthentication(info.getSubject());
            }
        }
        filterChain.doFilter(req, res);
    }

    // 인증 처리
    public void setAuthentication(String email) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(email);
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    // 인증 객체 생성
    private Authentication createAuthentication(String email) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}