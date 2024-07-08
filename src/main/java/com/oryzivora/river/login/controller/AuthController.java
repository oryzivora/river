package com.oryzivora.river.login.controller;

import com.oryzivora.river.login.constant.ERole;
import com.oryzivora.river.login.dao.SysRoleMapper;
import com.oryzivora.river.login.dao.SysUserMapper;
import com.oryzivora.river.login.dao.SysUserRoleMapper;
import com.oryzivora.river.login.model.SysRole;
import com.oryzivora.river.login.model.SysUser;
import com.oryzivora.river.login.model.SysUserRole;
import com.oryzivora.river.login.payload.request.LoginRequest;
import com.oryzivora.river.login.payload.request.SignupRequest;
import com.oryzivora.river.login.payload.response.MessageResponse;
import com.oryzivora.river.login.payload.response.UserInfoResponse;
import com.oryzivora.river.login.security.jwt.JwtUtils;
import com.oryzivora.river.login.security.services.UserDetailsImpl;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

//for Angular Client (withCredentials)
//@CrossOrigin(origins = "http://localhost:8081", maxAge = 3600, allowCredentials="true")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
  @Resource
  AuthenticationManager authenticationManager;

  @Resource
  SysUserMapper sysUserMapper;

  @Resource
  SysRoleMapper sysRoleMapper;

  @Resource
  SysUserRoleMapper sysUserRoleMapper;

  @Resource
  PasswordEncoder encoder;

  @Resource
  JwtUtils jwtUtils;

  @PostMapping("/signIn")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
    Authentication authentication = authenticationManager
        .authenticate(usernamePasswordAuthenticationToken);

    SecurityContextHolder.getContext().setAuthentication(authentication);

    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

    ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

    List<String> roles = userDetails.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.toList());

    return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
        .body(new UserInfoResponse(userDetails.getId(),
                                   userDetails.getUsername(),
                                   userDetails.getEmail(),
                                   roles));
  }

  /**
   * 注册方法
   * @param signUpRequest
   * @return
   */
  @PostMapping("/signup")
  @Transactional
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
    if (sysUserMapper.existsByUsername(signUpRequest.getUsername())) {
      return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
    }

    if (sysUserMapper.existsByEmail(signUpRequest.getEmail())) {
      return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
    }

    // Create new user's account
    SysUser user = new SysUser(signUpRequest.getUsername(),
                         signUpRequest.getEmail(),
                         encoder.encode(signUpRequest.getPassword()));

//    Set<String> strRoles = signUpRequest.getRole();
//    Set<SysRole> roles = new HashSet<>();

//    if (strRoles == null) {
      SysRole userRole = sysRoleMapper.findByName(ERole.ROLE_USER)
          .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//    } else {
//      strRoles.forEach(role -> {
//        switch (role) {
//        case "admin":
//          SysRole adminRole = sysRoleMapper.findByName(ERole.ROLE_ADMIN)
//              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//          roles.add(adminRole);
//
//          break;
//        case "mod":
//          SysRole modRole = sysRoleMapper.findByName(ERole.ROLE_MODERATOR)
//              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//          roles.add(modRole);
//
//          break;
//        default:
//          SysRole userRole = sysRoleMapper.findByName(ERole.ROLE_USER)
//              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//          roles.add(userRole);
//        }
//      });
//    }
    // TODO 需要自己实现
    sysUserMapper.insert(user);
    SysUserRole sysUserRole = new SysUserRole();
    sysUserRole.setUserId(user.getId());
    sysUserRole.setRoleId(userRole.getId());
    sysUserRoleMapper.insert(sysUserRole);

    return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
  }

  @PostMapping("/signout")
  public ResponseEntity<?> logoutUser() {
    ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
    return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
        .body(new MessageResponse("You've been signed out!"));
  }



  @PostMapping("/tttt")
  public ResponseEntity<?> tttt() {
    return ResponseEntity.ok("yes");
  }
}
