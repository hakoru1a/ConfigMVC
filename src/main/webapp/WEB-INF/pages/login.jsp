<%-- 
    Document   : login
    Created on : Jul 13, 2023, 10:41:40 AM
    Author     : DinhChuong
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<h1 class="text-center text-success">ĐĂNG NHẬP NGƯỜI DÙNG</h1>
<c:url value="/login" var="action" />
<form method="post" action="/login">
    <div class="form-floating mb-3 mt-3">
        <input type="text" class="form-control" id="username" placeholder="Tên đăng nhập" name="username">
        <label for="username">Tên đăng nhập</label>
    </div>

    <div class="form-floating mt-3 mb-3">
        <input type="password" class="form-control" id="pwd" placeholder="Mật khẩu" name="password">
        <label for="pwd">Mật khẩu</label>
    </div>

    <input type="submit" value="Đăng nhập" class="btn btn-danger" />
</form>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
    <head>
        <title>login</title>
    </head>
    <body>
        <h1>Spring MVC Security - Login with Google</h1>
        <h2>${message}</h2>

        <a href="https://accounts.google.com/o/oauth2/auth?scope=email&redirect_uri=http://localhost:8080/login-google&response_type=code&client_id=510556489503-gtmvki42khmh32mfnrl51qunq5islnvt.apps.googleusercontent.com&approval_prompt=force">Login With Google</a>  
        <a href="https://www.facebook.com/dialog/oauth?client_id=1352583565680412&redirect_uri=http://localhost:8080/login-facebook">Login Facebook</a>
        <form name='loginForm' action="<c:url value='j_spring_security_login' />" method='POST'>
            <table>
                <tr>
                    <td>User:</td>
                    <td><input type='text' name='username'></td>
                </tr>
                <tr>
                    <td>Password:</td>
                    <td><input type='password' name='password' /></td>
                </tr>
                <tr>
                    <td colspan='2'><input name="submit" type="submit" value="login" /></td>
                </tr>
            </table>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
        </form>
    </body>
</html>