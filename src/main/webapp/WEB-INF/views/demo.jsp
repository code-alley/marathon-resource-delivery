<!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html lang="en">
<head>
<link rel="stylesheet" href="${pageContext.request.contextPath}/webjars/bootstrap/3.3.1/css/bootstrap.min.css">
<script src="${pageContext.request.contextPath}/webjars/jquery/2.1.3/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/webjars/bootstrap/3.3.1/js/bootstrap.min.js"></script>
<script type="application/javascript">
	function ajaxCall(method, ajaxurl, viewer)
	{
		$("#"+viewer).html("<p></p>");
		var result;
		try
		{
			result = $.ajax({
			  crossDomain: true,
			  type:     method,
			  url:      ajaxurl,
			  //dataType: "jsonp",
			  dataType: "json",
			  success: function(json){
				//alert('success');
				$("#"+viewer).text(JSON.stringify(json));
			  },
			  error: function (jqXHR, exception) {
				  if (jqXHR.status === 0) {
		                alert('Not connect.\n Verify Network.');
		            } else if (jqXHR.status == 404) {
		                alert('Requested page not found. [404]');
		            } else if (jqXHR.status == 500) {
		                alert('Internal Server Error [500].');
		            } else if (exception === 'parsererror') {
		                alert('Requested JSON parse failed.');
		                alert(exception);
		            } else if (exception === 'timeout') {
		                alert('Time out error.');
		            } else if (exception === 'abort') {
		                alert('Ajax request aborted.');
		            } else {
		                alert('Uncaught Error.\n' + jqXHR.responseText);
		            }
              }
			});
			return result;
		}
		catch (err)
		{
			alert(err);
		}
			
		return null;
	}
</script>
</head>
<body>
	<jsp:directive.page contentType="text/html;charset=UTF-8"/>
	<c:set var="a_host" value="http://localhost:8080/delivery/v1" />
	<!-- BEGIN ACCORDION PORTLET-->
	<div class="portlet box blue">
		<div class="portlet-title">
			<div class="caption">
				<i class="fa fa-reorder"><jsp:text /></i>Docker control APIs
			</div>
			<div class="tools">
				<a href="javascript:;" class="collapse"><jsp:text /></a>
			</div>
		</div>
		<div class="portlet-body">
			<div class="panel-group accordion_api" id="accordion_api">

				<div class="panel panel-default">
					<div class="panel-heading">
						<h4 class="panel-title">
						<a class="accordion-toggle accordion-toggle-styled collasped" data-toggle="collapse" data-parent="#accordion_api" href="#collapse_api_0">
							 Group 생성
						</a>
						</h4>
					</div>
					<div id="collapse_api_0" class="panel-collapse collapse">
						<div class="panel-body">
							<p>
								Method : POST<br/>
								URL : ${a_host}/group/{groupId}?userId={userId}<br/>
								Sample : <a href="javascript:ajaxCall('POST', '${a_host}/group/demogroup?userId=inslab.dev', 'api_response_0');">
											POST ${a_host}/group/demogroup?userId=inslab.dev
										</a>								
							</p>
							<div class="note note-success" id="api_response_0">
								<p><jsp:text /></p>
							</div>
						</div>
					</div>
				</div>
				
				<div class="panel panel-default">
					<div class="panel-heading">
						<h4 class="panel-title">
						<a class="accordion-toggle accordion-toggle-styled collasped" data-toggle="collapse" data-parent="#accordion_api" href="#collapse_api_1">
							 Group 삭제
						</a>
						</h4>
					</div>
					<div id="collapse_api_1" class="panel-collapse collapse">
						<div class="panel-body">
							<p>
								Method : DELETE<br/>
								URL : ${a_host}/group/{groupId}<br/>
								Sample : <a href="javascript:ajaxCall('DELETE', '${a_host}/group/demogroup', 'api_response_1');">
											DELETE ${a_host}/group/demogroup
										</a>								
							</p>
							<div class="note note-success" id="api_response_1">
								<p><jsp:text /></p>
							</div>
						</div>
					</div>
				</div>
				
				<div class="panel panel-default">
					<div class="panel-heading">
						<h4 class="panel-title">
						<a class="accordion-toggle accordion-toggle-styled collasped" data-toggle="collapse" data-parent="#accordion_api" href="#collapse_api_2">
							 Group 정보
						</a>
						</h4>
					</div>
					<div id="collapse_api_2" class="panel-collapse collapse">
						<div class="panel-body">
							<p>
								Method : GET<br/>
								URL : ${a_host}/group/{groupId}<br/>
								Sample : <a href="javascript:ajaxCall('GET', '${a_host}/group/demogroup', 'api_response_2');">
											GET ${a_host}/group/demogroup
										</a>								
							</p>
							<div class="note note-success" id="api_response_2">
								<p><jsp:text /></p>
							</div>
						</div>
					</div>
				</div>
				
				<div class="panel panel-default">
					<div class="panel-heading">
						<h4 class="panel-title">
						<a class="accordion-toggle accordion-toggle-styled collasped" data-toggle="collapse" data-parent="#accordion_api" href="#collapse_domain_1">
							 User domain 정보
						</a>
						</h4>
					</div>
					<div id="collapse_domain_1" class="panel-collapse collapse">
						<div class="panel-body">
							<p>
								Method : GET<br/>
								URL : ${a_host}/domains/{userId}<br/>
								Sample : <a href="javascript:ajaxCall('GET', '${a_host}/domains/inslab.dev', 'domain_response_1');">
											GET ${a_host}/domains/inslab.dev
										</a>								
							</p>
							<div class="note note-success" id="domain_response_1">
								<p><jsp:text /></p>
							</div>
						</div>
					</div>
				</div>
				
				<div class="panel panel-default">
					<div class="panel-heading">
						<h4 class="panel-title">
						<a class="accordion-toggle accordion-toggle-styled collasped" data-toggle="collapse" data-parent="#accordion_api" href="#collapse_all_1">
							 All applications
						</a>
						</h4>
					</div>
					<div id="collapse_all_1" class="panel-collapse collapse">
						<div class="panel-body">
							<p>
								Method : GET<br/>
								URL : ${a_host}/app/{groupId}<br/>
								Sample : <a href="javascript:ajaxCall('GET', '${a_host}/app/demogroup', 'all_response_1');">
											GET ${a_host}/app/demogroup
										</a>								
							</p>
							<div class="note note-success" id="all_response_1">
								<p><jsp:text /></p>
							</div>
						</div>
					</div>
				</div>
				
				<div class="panel panel-default">
					<div class="panel-heading">
						<h4 class="panel-title">
						<a class="accordion-toggle accordion-toggle-styled collasped" data-toggle="collapse" data-parent="#accordion_api" href="#collapse_jenkins_1">
							 Jenkins 생성
						</a>
						</h4>
					</div>
					<div id="collapse_jenkins_1" class="panel-collapse collapse">
						<div class="panel-body">
							<p>
								Method : POST<br/>
								URL : ${a_host}/app/{groupId}/jenkins/{name}?description={description}<br/>
								Sample : <a href="javascript:ajaxCall('POST', '${a_host}/app/demogroup/jenkins/demojenkins?description=demo', 'jenkins_response_1');">
											POST ${a_host}/app/demogroup/jenkins/demojenkins?description=demo
										</a>								
							</p>
							<div class="note note-success" id="jenkins_response_1">
								<p><jsp:text /></p>
							</div>
						</div>
					</div>
				</div>
				
				<div class="panel panel-default">
					<div class="panel-heading">
						<h4 class="panel-title">
						<a class="accordion-toggle accordion-toggle-styled collasped" data-toggle="collapse" data-parent="#accordion_api" href="#collapse_jenkins_2">
							 Jenkins 삭제
						</a>
						</h4>
					</div>
					<div id="collapse_jenkins_2" class="panel-collapse collapse">
						<div class="panel-body">
							<p>
								Method : DELETE<br/>
								URL : ${a_host}/app/{groupId}/jenkins/{name}<br/>
								Sample : <a href="javascript:ajaxCall('DELETE', '${a_host}/app/demogroup/jenkins/demojenkins', 'jenkins_response_2');">
											DELETE ${a_host}/app/demogroup/jenkins/demojenkins
										</a>								
							</p>
							<div class="note note-success" id="jenkins_response_2">
								<p><jsp:text /></p>
							</div>
						</div>
					</div>
				</div>
				
				<div class="panel panel-default">
					<div class="panel-heading">
						<h4 class="panel-title">
						<a class="accordion-toggle accordion-toggle-styled collasped" data-toggle="collapse" data-parent="#accordion_api" href="#collapse_jenkins_4">
							 Jenkins 정보 (name) 
						</a>
						</h4>
					</div>
					<div id="collapse_jenkins_4" class="panel-collapse collapse">
						<div class="panel-body">
							<p>
								Method : GET<br/>
								URL : ${a_host}/app/{groupId}/jenkins/{name}<br/>
								Sample : <a href="javascript:ajaxCall('GET', '${a_host}/app/demogroup/jenkins/demojenkins', 'jenkins_response_4');">
											GET ${a_host}/app/demogroup/jenkins/demojenkins
										</a>								
							</p>
							<div class="note note-success" id="jenkins_response_4">
								<p><jsp:text /></p>
							</div>
						</div>
					</div>
				</div>
				
				<div class="panel panel-default">
					<div class="panel-heading">
						<h4 class="panel-title">
						<a class="accordion-toggle accordion-toggle-styled collasped" data-toggle="collapse" data-parent="#accordion_api" href="#collapse_mysql_1">
							 MySql 생성
						</a>
						</h4>
					</div>
					<div id="collapse_mysql_1" class="panel-collapse collapse">
						<div class="panel-body">
							<p>
								Method : POST<br/>
								URL : ${a_host}/app/{groupId}/mysql/{name}?description={description}&username={username}&password={password}<br/>
								Sample : <a href="javascript:ajaxCall('POST', '${a_host}/app/demogroup/mysql/demomysql?description=demo&username=inslab.dev&password=1234', 'mysql_response_1');">
											POST ${a_host}/app/demogroup/mysql/demomysql?description=demo&username=inslab.dev&password=1234
										</a>								
							</p>
							<div class="note note-success" id="mysql_response_1">
								<p><jsp:text /></p>
							</div>
						</div>
					</div>
				</div>
				
				<div class="panel panel-default">
					<div class="panel-heading">
						<h4 class="panel-title">
						<a class="accordion-toggle accordion-toggle-styled collasped" data-toggle="collapse" data-parent="#accordion_api" href="#collapse_mysql_2">
							 MySql 삭제
						</a>
						</h4>
					</div>
					<div id="collapse_mysql_2" class="panel-collapse collapse">
						<div class="panel-body">
							<p>
								Method : DELETE<br/>
								URL : ${a_host}/app/{groupId}/mysql/{name}<br/>
								Sample : <a href="javascript:ajaxCall('DELETE', '${a_host}/app/demogroup/mysql/demomysql', 'mysql_response_2');">
											DELETE ${a_host}/app/demogroup/mysql/demomysql
										</a>								
							</p>
							<div class="note note-success" id="mysql_response_2">
								<p><jsp:text /></p>
							</div>
						</div>
					</div>
				</div>
				
				<div class="panel panel-default">
					<div class="panel-heading">
						<h4 class="panel-title">
						<a class="accordion-toggle accordion-toggle-styled collasped" data-toggle="collapse" data-parent="#accordion_api" href="#collapse_mysql_4">
							 MySql 정보 (name)
						</a>
						</h4>
					</div>
					<div id="collapse_mysql_4" class="panel-collapse collapse">
						<div class="panel-body">
							<p>
								Method : GET<br/>
								URL : ${a_host}/app/{groupId}/mysql/{name}<br/>
								Sample : <a href="javascript:ajaxCall('GET', '${a_host}/app/demogroup/mysql/demomysql', 'mysql_response_4');">
											GET ${a_host}/app/demogroup/mysql/demomysql
										</a>								
							</p>
							<div class="note note-success" id="mysql_response_4">
								<p><jsp:text /></p>
							</div>
						</div>
					</div>
				</div>
				
				<div class="panel panel-default">
					<div class="panel-heading">
						<h4 class="panel-title">
						<a class="accordion-toggle accordion-toggle-styled collasped" data-toggle="collapse" data-parent="#accordion_api" href="#collapse_gitblit_1">
							 Gitblit 생성
						</a>
						</h4>
					</div>
					<div id="collapse_gitblit_1" class="panel-collapse collapse">
						<div class="panel-body">
							<p>
								Method : POST<br/>
								URL : ${a_host}/app/{groupId}/gitblit/{name}?description={description}<br/>
								Sample : <a href="javascript:ajaxCall('POST', '${a_host}/app/demogroup/gitblit/demogitblit?description=demo', 'gitblit_response_1');">
											POST ${a_host}/app/demogroup/gitblit/demogitblit?description=demo
										</a>								
							</p>
							<div class="note note-success" id="gitblit_response_1">
								<p><jsp:text /></p>
							</div>
						</div>
					</div>
				</div>
				
				<div class="panel panel-default">
					<div class="panel-heading">
						<h4 class="panel-title">
						<a class="accordion-toggle accordion-toggle-styled collasped" data-toggle="collapse" data-parent="#accordion_api" href="#collapse_gitblit_2">
							 Gitblit 삭제
						</a>
						</h4>
					</div>
					<div id="collapse_gitblit_2" class="panel-collapse collapse">
						<div class="panel-body">
							<p>
								Method : DELETE<br/>
								URL : ${a_host}/app/{groupId}/gitblit/{name}<br/>
								Sample : <a href="javascript:ajaxCall('DELETE', '${a_host}/app/demogroup/gitblit/demogitblit', 'gitblit_response_2');">
											DELETE ${a_host}/app/demogroup/gitblit/demogitblit
										</a>								
							</p>
							<div class="note note-success" id="gitblit_response_2">
								<p><jsp:text /></p>
							</div>
						</div>
					</div>
				</div>
				
				<div class="panel panel-default">
					<div class="panel-heading">
						<h4 class="panel-title">
						<a class="accordion-toggle accordion-toggle-styled collasped" data-toggle="collapse" data-parent="#accordion_api" href="#collapse_gitblit_4">
							 Gitblit 정보 (name)
						</a>
						</h4>
					</div>
					<div id="collapse_gitblit_4" class="panel-collapse collapse">
						<div class="panel-body">
							<p>
								Method : GET<br/>
								URL : ${a_host}/app/{groupId}/gitblit/{name}<br/>
								Sample : <a href="javascript:ajaxCall('GET', '${a_host}/app/demogroup/gitblit/demogitblit', 'gitblit_response_4');">
											GET ${a_host}/app/demogroup/gitblit/demogitblit
										</a>								
							</p>
							<div class="note note-success" id="gitblit_response_4">
								<p><jsp:text /></p>
							</div>
						</div>
					</div>
				</div>
				
				<div class="panel panel-default">
					<div class="panel-heading">
						<h4 class="panel-title">
						<a class="accordion-toggle accordion-toggle-styled collasped" data-toggle="collapse" data-parent="#accordion_api" href="#collapse_redmine_1">
							 Redmine 생성
						</a>
						</h4>
					</div>
					<div id="collapse_redmine_1" class="panel-collapse collapse">
						<div class="panel-body">
							<p>
								Method : POST<br/>
								URL : ${a_host}/app/{groupId}/redmine/{name}?dbtype={dbtype}&dbhost={dbhost}&dbport={dbport}&dbname={dbname}&username={username}&password={password}&description={description}<br/>
								Sample : <a href="javascript:ajaxCall('POST', '${a_host}/app/demogroup/redmine/demoredmine?dbtype=mysql&dbhost=10.1.100.8&dbport=10001&dbname=redmine&username=inslab.dev&password=1234&description=demo', 'redmine_response_1');">
											POST ${a_host}/app/demogroup/redmine/demoredmine?dbtype=mysql&dbhost=10.1.100.8&dbport=10001&dbname=redmine&username=inslab.dev&password=1234&description=demo
										</a>
							</p>
							<div class="note note-success" id="redmine_response_1">
								<p><jsp:text /></p>
							</div>
						</div>
					</div>
				</div>
				
				<div class="panel panel-default">
					<div class="panel-heading">
						<h4 class="panel-title">
						<a class="accordion-toggle accordion-toggle-styled collasped" data-toggle="collapse" data-parent="#accordion_api" href="#collapse_redmine_2">
							 Redmine 삭제
						</a>
						</h4>
					</div>
					<div id="collapse_redmine_2" class="panel-collapse collapse">
						<div class="panel-body">
							<p>
								Method : DELETE<br/>
								URL : ${a_host}/app/{groupId}/redmine/{name}<br/>
								Sample : <a href="javascript:ajaxCall('DELETE', '${a_host}/app/demogroup/redmine/demoredmine', 'redmine_response_2');">
											DELETE ${a_host}/app/demogroup/redmine/demoredmine
										</a>								
							</p>
							<div class="note note-success" id="redmine_response_2">
								<p><jsp:text /></p>
							</div>
						</div>
					</div>
				</div>
				
				<div class="panel panel-default">
					<div class="panel-heading">
						<h4 class="panel-title">
						<a class="accordion-toggle accordion-toggle-styled collasped" data-toggle="collapse" data-parent="#accordion_api" href="#collapse_redmine_4">
							 Redmine 정보 (name)
						</a>
						</h4>
					</div>
					<div id="collapse_redmine_4" class="panel-collapse collapse">
						<div class="panel-body">
							<p>
								Method : GET<br/>
								URL : ${a_host}/app/{groupId}/redmine/{name}<br/>
								Sample : <a href="javascript:ajaxCall('GET', '${a_host}/app/demogroup/redmine/demoredmine', 'redmine_response_4');">
											GET ${a_host}/app/demogroup/redmine/demoredmine
										</a>								
							</p>
							<div class="note note-success" id="redmine_response_4">
								<p><jsp:text /></p>
							</div>
						</div>
					</div>
				</div>
				
				<div class="panel panel-default">
					<div class="panel-heading">
						<h4 class="panel-title">
						<a class="accordion-toggle accordion-toggle-styled collasped" data-toggle="collapse" data-parent="#accordion_api" href="#collapse_xwiki_1">
							 Xwiki 생성
						</a>
						</h4>
					</div>
					<div id="collapse_xwiki_1" class="panel-collapse collapse">
						<div class="panel-body">
							<p>
								Method : POST<br/>
								URL : ${a_host}/app/{groupId}/xwiki/{name}<br/>
								Sample : <a href="javascript:ajaxCall('POST', '${a_host}/app/demogroup/xwiki/demoxwiki?dbtype=mysql&dbhost=10.1.100.8&dbport=10001&dbname=xwiki&username=inslab.dev&password=1234&description=demo', 'xwiki_response_1');">
											POST ${a_host}/app/demogroup/xwiki/demoxwiki?dbtype=mysql&dbhost=10.1.100.8&dbport=10001&dbname=xwiki&username=inslab.dev&password=1234&description=demo
										</a>
							</p>
							<div class="note note-success" id="xwiki_response_1">
								<p><jsp:text /></p>
							</div>
						</div>
					</div>
				</div>
				
				<div class="panel panel-default">
					<div class="panel-heading">
						<h4 class="panel-title">
						<a class="accordion-toggle accordion-toggle-styled collasped" data-toggle="collapse" data-parent="#accordion_api" href="#collapse_xwiki_2">
							 Xwiki 삭제
						</a>
						</h4>
					</div>
					<div id="collapse_xwiki_2" class="panel-collapse collapse">
						<div class="panel-body">
							<p>
								Method : DELETE<br/>
								URL : ${a_host}/app/{groupId}/xwiki/{name}<br/>
								Sample : <a href="javascript:ajaxCall('DELETE', '${a_host}/app/demogroup/xwiki/demoxwiki', 'xwiki_response_2');">
											DELETE ${a_host}/app/demogroup/xwiki/demoxwiki
										</a>								
							</p>
							<div class="note note-success" id="xwiki_response_2">
								<p><jsp:text /></p>
							</div>
						</div>
					</div>
				</div>
				
				<div class="panel panel-default">
					<div class="panel-heading">
						<h4 class="panel-title">
						<a class="accordion-toggle accordion-toggle-styled collasped" data-toggle="collapse" data-parent="#accordion_api" href="#collapse_xwiki_4">
							 Xwiki 정보 (name)
						</a>
						</h4>
					</div>
					<div id="collapse_xwiki_4" class="panel-collapse collapse">
						<div class="panel-body">
							<p>
								Method : GET<br/>
								URL : ${a_host}/app/{groupId}/xwiki/{name}<br/>
								Sample : <a href="javascript:ajaxCall('GET', '${a_host}/app/demogroup/xwiki/demoxwiki', 'xwiki_response_4');">
											GET ${a_host}/app/demogroup/xwiki/demoxwiki
										</a>								
							</p>
							<div class="note note-success" id="xwiki_response_4">
								<p><jsp:text /></p>
							</div>
						</div>
					</div>
				</div>
				
				<div class="panel panel-default">
					<div class="panel-heading">
						<h4 class="panel-title">
						<a class="accordion-toggle accordion-toggle-styled collasped" data-toggle="collapse" data-parent="#accordion_api" href="#collapse_testlink_1">
							 Testlink 생성
						</a>
						</h4>
					</div>
					<div id="collapse_testlink_1" class="panel-collapse collapse">
						<div class="panel-body">
							<p>
								Method : POST<br/>
								URL : ${a_host}/app/{groupId}/testlink/{name}?password={password}&description={description}<br/>
								Sample : <a href="javascript:ajaxCall('POST', '${a_host}/app/demogroup/testlink/demotestlink?password=1234&description=demo', 'testlink_response_1');">
											POST ${a_host}/app/demogroup/testlink/demotestlink?password=1234&description=demo
										</a>
							</p>
							<div class="note note-success" id="testlink_response_1">
								<p><jsp:text /></p>
							</div>
						</div>
					</div>
				</div>
				
				<div class="panel panel-default">
					<div class="panel-heading">
						<h4 class="panel-title">
						<a class="accordion-toggle accordion-toggle-styled collasped" data-toggle="collapse" data-parent="#accordion_api" href="#collapse_testlink_2">
							 Testlink 삭제
						</a>
						</h4>
					</div>
					<div id="collapse_testlink_2" class="panel-collapse collapse">
						<div class="panel-body">
							<p>
								Method : DELETE<br/>
								URL : ${a_host}/app/{groupId}/testlink/{name}<br/>
								Sample : <a href="javascript:ajaxCall('DELETE', '${a_host}/app/demogroup/testlink/demotestlink', 'testlink_response_2');">
											DELETE ${a_host}/app/demogroup/testlink/demotestlink
										</a>								
							</p>
							<div class="note note-success" id="testlink_response_2">
								<p><jsp:text /></p>
							</div>
						</div>
					</div>
				</div>
				
				<div class="panel panel-default">
					<div class="panel-heading">
						<h4 class="panel-title">
						<a class="accordion-toggle accordion-toggle-styled collasped" data-toggle="collapse" data-parent="#accordion_api" href="#collapse_testlink_4">
							 Testlink 정보 (name)
						</a>
						</h4>
					</div>
					<div id="collapse_testlink_4" class="panel-collapse collapse">
						<div class="panel-body">
							<p>
								Method : GET<br/>
								URL : ${a_host}/app/{groupId}/testlink/{name}<br/>
								Sample : <a href="javascript:ajaxCall('GET', '${a_host}/app/demogroup/testlink/demotestlink', 'testlink_response_4');">
											GET ${a_host}/app/demogroup/testlink/demotestlink
										</a>								
							</p>
							<div class="note note-success" id="testlink_response_4">
								<p><jsp:text /></p>
							</div>
						</div>
					</div>
				</div>
				
				<div class="panel panel-default">
					<div class="panel-heading">
						<h4 class="panel-title">
						<a class="accordion-toggle accordion-toggle-styled collasped" data-toggle="collapse" data-parent="#accordion_api" href="#collapse_sonarqube_1">
							 Sonarqube 생성
						</a>
						</h4>
					</div>
					<div id="collapse_sonarqube_1" class="panel-collapse collapse">
						<div class="panel-body">
							<p>
								Method : POST<br/>
								URL : ${a_host}/app/{groupId}/sonarqube/{name}?dbtype={dbtype}&dbhost={dbhost}&dbport={dbport}&dbname={dbname}&username={username}&password={password}&description={description}<br/>
								Sample : <a href="javascript:ajaxCall('POST', '${a_host}/app/demogroup/sonarqube/demosonarqube?dbtype=mysql&dbhost=10.1.100.8&dbport=10001&dbname=sonar&username=inslab.dev&password=1234&description=demo', 'sonarqube_response_1');">
											POST ${a_host}/app/demogroup/sonarqube/demosonarqube?dbtype=mysql&dbhost=10.1.100.8&dbport=10001&dbname=sonar&username=inslab.dev&password=1234&description=demo
										</a>
							</p>
							<div class="note note-success" id="sonarqube_response_1">
								<p><jsp:text /></p>
							</div>
						</div>
					</div>
				</div>
				
				<div class="panel panel-default">
					<div class="panel-heading">
						<h4 class="panel-title">
						<a class="accordion-toggle accordion-toggle-styled collasped" data-toggle="collapse" data-parent="#accordion_api" href="#collapse_sonarqube_2">
							 Sonarqube 삭제
						</a>
						</h4>
					</div>
					<div id="collapse_sonarqube_2" class="panel-collapse collapse">
						<div class="panel-body">
							<p>
								Method : DELETE<br/>
								URL : ${a_host}/app/{groupId}/sonarqube/{name}<br/>
								Sample : <a href="javascript:ajaxCall('DELETE', '${a_host}/app/demogroup/sonarqube/demosonarqube', 'sonarqube_response_2');">
											DELETE ${a_host}/app/demogroup/sonarqube/demosonarqube
										</a>
							</p>
							<div class="note note-success" id="sonarqube_response_2">
								<p><jsp:text /></p>
							</div>
						</div>
					</div>
				</div>
				
				<div class="panel panel-default">
					<div class="panel-heading">
						<h4 class="panel-title">
						<a class="accordion-toggle accordion-toggle-styled collasped" data-toggle="collapse" data-parent="#accordion_api" href="#collapse_sonarqube_4">
							 Sonarqube 정보 (name)
						</a>
						</h4>
					</div>
					<div id="collapse_sonarqube_4" class="panel-collapse collapse">
						<div class="panel-body">
							<p>
								Method : GET<br/>
								URL : ${a_host}/app/{groupId}/sonarqube/{name}<br/>
								Sample : <a href="javascript:ajaxCall('GET', '${a_host}/app/demogroup/sonarqube/demosonarqube', 'sonarqube_response_4');">
											GET ${a_host}/app/demogroup/sonarqube/demosonarqube
										</a>								
							</p>
							<div class="note note-success" id="sonarqube_response_4">
								<p><jsp:text /></p>
							</div>
						</div>
					</div>
				</div>
				
				<div class="panel panel-default">
					<div class="panel-heading">
						<h4 class="panel-title">
						<a class="accordion-toggle accordion-toggle-styled collasped" data-toggle="collapse" data-parent="#accordion_api" href="#collapse_reviewboard_1">
							 Reviewboard 생성
						</a>
						</h4>
					</div>
					<div id="collapse_reviewboard_1" class="panel-collapse collapse">
						<div class="panel-body">
							<p>
								Method : POST<br/>
								URL : ${a_host}/app/{groupId}/reviewboard/{name}?description={description}<br/>
								Sample : <a href="javascript:ajaxCall('POST', '${a_host}/app/demogroup/reviewboard/demoreviewboard?description=demo', 'reviewboard_response_1');">
											POST ${a_host}/app/demogroup/reviewboard/demoreviewboard?description=demo
										</a>
							</p>
							<div class="note note-success" id="reviewboard_response_1">
								<p><jsp:text /></p>
							</div>
						</div>
					</div>
				</div>
				
				<div class="panel panel-default">
					<div class="panel-heading">
						<h4 class="panel-title">
						<a class="accordion-toggle accordion-toggle-styled collasped" data-toggle="collapse" data-parent="#accordion_api" href="#collapse_reviewboard_2">
							 Reviewboard 삭제
						</a>
						</h4>
					</div>
					<div id="collapse_reviewboard_2" class="panel-collapse collapse">
						<div class="panel-body">
							<p>
								Method : DELETE<br/>
								URL : ${a_host}/app/{groupId}/reviewboard/{name}<br/>
								Sample : <a href="javascript:ajaxCall('DELETE', '${a_host}/app/demogroup/reviewboard/demoreviewboard', 'reviewboard_response_2');">
											DELETE ${a_host}/app/demogroup/reviewboard/demoreviewboard
										</a>
							</p>
							<div class="note note-success" id="reviewboard_response_2">
								<p><jsp:text /></p>
							</div>
						</div>
					</div>
				</div>
				
				<div class="panel panel-default">
					<div class="panel-heading">
						<h4 class="panel-title">
						<a class="accordion-toggle accordion-toggle-styled collasped" data-toggle="collapse" data-parent="#accordion_api" href="#collapse_reviewboard_4">
							 Reviewboard 정보 (name)
						</a>
						</h4>
					</div>
					<div id="collapse_reviewboard_4" class="panel-collapse collapse">
						<div class="panel-body">
							<p>
								Method : GET<br/>
								URL : ${a_host}/app/{groupId}/reviewboard/{name}<br/>
								Sample : <a href="javascript:ajaxCall('GET', '${a_host}/app/demogroup/reviewboard/demoreviewboard', 'reviewboard_response_4');">
											GET ${a_host}/app/demogroup/reviewboard/demoreviewboard
										</a>								
							</p>
							<div class="note note-success" id="reviewboard_response_4">
								<p><jsp:text /></p>
							</div>
						</div>
					</div>
				</div>
				
			</div>
		</div>
	</div>
	<!-- END ACCORDION PORTLET-->
</body>
</html>
