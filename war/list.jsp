<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="example.guestbook.GuestbookEntry"%>
<%@ page import="java.util.List"%>
<%
	response.setHeader("Pragma", "No-cache"); //HTTP 1.0
	response.setDateHeader("Expires", 0);
	response.setHeader("Cache-Control", "no-cache");
%>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Google App Engine Guestbook</title>
<link href="/style.css" rel="stylesheet" type="text/css" media="screen" />
<script type="text/javascript">
function sendEmail( email) {
	mailContent = prompt("이메일 내용을 입력하세요", "방문해 주셔서 고맙습니다.");
	if( mailContent != null ) {
		document.forms['send_mail'].email.value= email;
		document.forms['send_mail'].content.value = mailContent;
		document.forms['send_mail'].submit();
	}
	return false;
}

function deleteItem( id ) {
	deleteConfirm = confirm('정말로 삭제하시겠습니까?');
	if( deleteConfirm ) {
	 	document.location.href='delete?id='+id;
	} else {
		return false;
	}
}
</script>
<style type="text/css">
.entry .writer .name {
	text-decoration: none;
	text-transform: capitalize;
	font-size: 30px;
	color: #1E3F7F;
	padding-right: 20px;
}

.entry .writer .write_date {
	padding-left: 20px;
	padding-right: 20px;
	color: #aaa;
}

.entry .comment {
	background-color: #eee;
	padding: 10px 20px 10px 20px;
}
</style>
</head>
<body>
	<div id="header">
		<div id="logo">
			<h1>
				<a href="#">Google App Engine Guestbook</a>
			</h1>
			<h2>Leave a message!</h2>
		</div>
		<hr />
	</div>
	<div id="page">
		<div id="page-bgtop">
			<div id="page-bgbtm">
				<div id="content">
					<div class="post">
						<form id="input_entry" method="post" action="save">
							<div style="background-color: #eee; padding: 20px;">
								<p style="font-size: 30px; color: #1E3F7F; line-height: 100%;">Leave
									a new message!</p>
								<table>
									<tr>
										<th valign="top"><label for="name">Name:</label></th>
										<td><input type="text" id="name" name="name" tabindex="1" /></td>
										<th rowspan="2" valign="top"><label for="comment">Comment:</label></th>
										<td rowspan="2"><textarea id="comment" name="comment"
												cols="40" rows="3" tabindex="3"></textarea></td>
									</tr>
									<tr>
										<th valign="top"><label for="email">Email:</label></th>
										<td><input type="text" id="email" name="email"
											tabindex="2" /></td>
									</tr>
									<tr>
										<td colspan="4"><input type="submit"
											style="border: 1px solid #464646; background-color: #1E3F7F; color: white;"
											tabindex="4" /></td>
									</tr>
								</table>
							</div>
						</form>
						<form method="get" action="search">
						<div style="float: right;" >
							
								<p style="margin-bottom: 20px;">
									<label for="keyword">Name:</label> <input type="text" name="keyword"
										id="keyword" value="<%= request.getAttribute("keyword")%>"/><input
										style="border: 1px solid #464646; background-color: #1E3F7F; color: white;"
										type="submit" value="검색" />
								</p>
							
						</div>
						</form>
						<div class="entry">
							<%
								List<GuestbookEntry> list = (List<GuestbookEntry>) request
										.getAttribute("list");
							%>
							<%
								if (list != null && list.size() > 0) {
									for (GuestbookEntry entry : list) {
							%>
							<div name="entry" style="margin-bottom: 20px;">
								<p class="writer">
									<span class="name"><%=entry.getName()%></span><a href="#"
										onclick="return sendEmail( '<%=entry.getEmail()%>');"><%=entry.getEmail()%></a>
									<span class="write_date"><%=new java.text.SimpleDateFormat(
							"yyyy/MM/dd HH:mm:ss").format(entry.getDate())%></span><a href="#"
										onclick="return deleteItem( <%=entry.getId()%> );">delete</a>
								</p>
								<div class="comment"><%=entry.getComment()%></div>
							</div>

							<%
								}
								} else {
							%>
							entry is empty
							<%
								}
							%>
							<div style="display: none;">
								<form id="send_mail" method="post" action="send_mail">
									<input type="hidden" id="send_mail_id" name="email" value="" />
									<input type="hidden" id="send_mail_content" name="content"
										value="" />
								</form>
							</div>
						</div>
					</div>
				</div>
				<div style="clear: both;"></div>
			</div>
		</div>
</body>
</html>