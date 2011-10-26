<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="example.guestbook.GuestbookEntry" %>
<%@ page import="java.util.List" %>

<html>
<head>
<script type="text/javascript">
function sendEmail( email) {
var mailContent = prompt("이메일 내용을 입력하세요", "방문해 주셔서 고맙습니다.");
document.forms['send_mail'].email.value= email;
document.forms['send_mail'].content.value = mailContent;
document.forms['send_mail'].submit();
}
</script>
</head>
<body>
<h1>List of Entry</h1>
<form id="input_entry" method="post" action="save">
<div>
<ul>
<li><label for="name">Name:</label><input type="text" id="name" name="name"/></li>
<li><label for="comment">Comment:</label><input type="text" id="comment" name="comment"/></li>
<li><label for="email">Email:</label><input type="text" id="email" name="email"/></li>
<li><input type="submit"/>
</ul>
</div>
</form>
<% List<GuestbookEntry> list = (List<GuestbookEntry>) request.getAttribute("list"); %>
<% if( list != null && list.size() > 0 ) { 
 for( GuestbookEntry entry: list ) { %>
 <div name="entry">
 <ul><li>Id:<%= entry.getId() %></li>
 <li>Name:<%= entry.getName() %></li>
 <li>Comment:<%= entry.getComment() %></li>
 <li>Email:<a href="#" onclick="sendEmail( '<%= entry.getEmail() %>');"><%= entry.getEmail() %></a></li>
 <li>Date:<%= entry.getDate() %></li>
 </ul>
</div>
<% } } else { %>
entry is empty
<% } %>
<div style="display:none;">
<form id="send_mail" method="post" action="send_mail">
<input type="hidden" id="send_mail_id" name="email" value=""/>
<input type="hidden" id="send_mail_content" name="content" value=""/>
</form>
</div>
</body>
</html>