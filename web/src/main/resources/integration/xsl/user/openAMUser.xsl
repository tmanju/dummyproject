<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:user="http://schemas.thirdpillar.com/user" xmlns:ser="http://schemas.thirdpillar.com/user/service">
	<xsl:template match="/">
		<ser:CreateUser>
			<ser:Request>
				<xsl:for-each select="user">
					<xsl:call-template name="FillUser">
						<xsl:with-param name="user" select="."/>
					</xsl:call-template>
				</xsl:for-each>
				<xsl:for-each select="customer">
					<xsl:call-template name="FillCustomer">
						<xsl:with-param name="customer" select="."/>
					</xsl:call-template>
				</xsl:for-each>
			</ser:Request>
		</ser:CreateUser>
	</xsl:template>
	<xsl:template name="FillUser">
		<xsl:param name="user"/>
		<ser:User>
			<user:SourceSystem>
				<xsl:text>LP</xsl:text>
			</user:SourceSystem>
			<!--Optional: -->
			<user:SourceSystemUserName>
				<xsl:value-of select="$user/username"/>
			</user:SourceSystemUserName>
			<!--Optional: -->
			<user:SourceSystemIdentifier>
				<xsl:value-of select="$user/externalRefId"/>
				<!-- used for generating user certificate -->
			</user:SourceSystemIdentifier>
			<user:FirstName>
				<xsl:value-of select="$user/firstName"/>
			</user:FirstName>
			<!--Optional: -->
			<user:MiddleName>
				<xsl:value-of select="$user/middleInitial"/>
			</user:MiddleName>
			<user:LastName>
				<xsl:value-of select="$user/lastName"/>
			</user:LastName>
			<!--Optional: -->
			<user:Email>
				<xsl:value-of select="$user/contact/email"/>
			</user:Email>
		</ser:User>
		<ser:Action>		
			<xsl:choose>
				<xsl:when test="$user/createdInOpenAMDttm != '' ">
					<xsl:value-of select=" 'REPROVISION' "/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select=" 'PROVISION' "/>
				</xsl:otherwise>
			</xsl:choose>
		</ser:Action>
	</xsl:template>
	<xsl:template name="FillCustomer">
		<xsl:param name="customer"/>
		<ser:User>
			<user:SourceSystem>
				<xsl:text>SF</xsl:text>
			</user:SourceSystem>
			<!--Optional: -->
			<user:SourceSystemUserName>
				<xsl:value-of select="$customer/partyDetail/firstName"/>
			</user:SourceSystemUserName>
			<!--Optional: -->
			<user:SourceSystemIdentifier>
				<xsl:value-of select="$customer/salesForcecomId"/>
				<!-- used for generating user certificate -->
			</user:SourceSystemIdentifier>
			<user:FirstName>
				<xsl:value-of select="$customer/partyDetail/firstName"/>
			</user:FirstName>
			<!--Optional: -->
			<user:MiddleName>
				<xsl:value-of select="$customer/partyDetail/middleName"/>
			</user:MiddleName>
			<user:LastName>
				<xsl:value-of select="$customer/partyDetail/lastName"/>
			</user:LastName>
			<!--Optional: -->
			<user:Email>
				<xsl:value-of select="$customer/partyDetail/emailAddress"/>
			</user:Email>
		</ser:User>
		<ser:Action>		
			<xsl:choose>
				<xsl:when test="$customer/partyDetail/createdInOpenAMDttm != '' ">
					<xsl:value-of select=" 'REPROVISION' "/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select=" 'PROVISION' "/>
				</xsl:otherwise>
			</xsl:choose>
		</ser:Action>
	</xsl:template>
</xsl:stylesheet>
