<%--
  Created by IntelliJ IDEA.
  User: Alexander
  Date: 21.06.2015
  Time: 16:17
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>User Profile</title>
</head>

<body>
    <h1>User Profile</h1>
    <div class="profilePic>"
        <g:if test="${profile.photo}">
            <img src=""${createLink(controller: 'image', action: 'renderImage', id: profile.user.loginId)} />
        </g:if>
        <p>Profile for <strong>${profile.fullName}</strong></p>
        <p>Bio: ${profile.bioa}</p>

</body>
</html>