package com.ppdm.backend.websockets;


import com.ppdm.backend.security.config.JwtTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class JwtChannelInterceptor implements ChannelInterceptor {

    private static final String HEADER_JWT = "app-auth";
    private static final String STOMP_COMMAND_CONNECT = "CONNECT";

    @Autowired
    private JwtTokenService jwtTokenService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (STOMP_COMMAND_CONNECT.equals(accessor.getCommand() != null ? accessor.getCommand().name() : null)) {
            String token = accessor.getFirstNativeHeader(HEADER_JWT);
            if (token != null) {
                Authentication authentication = jwtTokenService.getAuthenticationFromToken(token);
                accessor.setUser(authentication);
            }
        }
        return message;
    }
}