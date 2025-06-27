package com.example.eMarketplace.security;


import com.example.eMarketplace.util.TokenUtils;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.SecurityContextRepository;

import java.io.IOException;
import java.io.PrintWriter;

import static com.example.eMarketplace.security.TokenAuthenticationFilter.TOKEN_HEADER;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TokenAuthenticationFilterTest {

    @Spy
    private TokenUtils tokenUtils = spy(new TokenUtils("043ba5b0-3694-4d11-af1d-efe6337a35", 30000));
    @Mock
    private SecurityContextRepository securityContextRepository;

    @InjectMocks
    private TokenAuthenticationFilter tokenAuthenticationFilter;

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private FilterChain chain;

    @Test
    void testDoFilterInternal() throws ServletException, IOException {
        var token = tokenUtils.generateToken("user");
        when(request.getHeader(TOKEN_HEADER)).thenReturn(token);
        doNothing().when(securityContextRepository).saveContext(any(SecurityContext.class), eq(request), eq(response));
        doNothing().when(chain).doFilter(request, response);

        tokenAuthenticationFilter.doFilterInternal(request, response, chain);

        verify(securityContextRepository).saveContext(any(SecurityContext.class), eq(request), eq(response));
        verify(chain).doFilter(request, response);
    }

    @Test
    void testDoFilterInternalMissingToken() throws ServletException, IOException {
        when(request.getHeader(TOKEN_HEADER)).thenReturn(null);
        doNothing().when(chain).doFilter(request, response);

        tokenAuthenticationFilter.doFilterInternal(request, response, chain);

        verify(securityContextRepository, times(0)).saveContext(any(SecurityContext.class), eq(request), eq(response));
        verify(chain, times(1)).doFilter(request, response);
    }

    @Test
    void testDoFilterInternalExpiredToken() throws ServletException, IOException {
        var token = tokenUtils.generateToken("user");
        when(request.getHeader(TOKEN_HEADER)).thenReturn(token);
        when(tokenUtils.getTokenUsername(token)).thenThrow(new JwtException("Expired token"));

        doNothing().when(response).setStatus(HttpServletResponse.SC_FORBIDDEN);
        when(response.getWriter()).thenReturn(mock(PrintWriter.class));

        tokenAuthenticationFilter.doFilterInternal(request, response, chain);

        verify(response).setStatus(HttpServletResponse.SC_FORBIDDEN);
        verify(securityContextRepository, never()).saveContext(any(SecurityContext.class), eq(request), eq(response));
        verify(chain, never()).doFilter(request, response);
    }

}