<%--
  Created by IntelliJ IDEA.
  User: Alexander
  Date: 21.06.2015
  Time: 17:06
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Sign into Hubbub</title>
    <meta name="layout" content="main">
</head>
<body>
<h1>Sign in</h1>
<g:form action="signIn">
    <fieldset class="form">
        <div class="fieldcontain required">
            <label for="loginId">Login ID</label>
            <g:textField name="loginId" value="${loginId}"/>
        </div>
        <div class="fieldcontain required">
            <label for="password">Password</label>
            <g:passwordField name="password"/>
        </div>
    </fieldset>
    <fieldset class="buttons">
        <g:submitButton name="signIn" value="Sign in"/>
    </fieldset>
</g:form>

</body>
</html>