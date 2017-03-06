<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:user="http://schemas.thirdpillar.com/user" xmlns:ser="http://schemas.thirdpillar.com/user/service" exclude-result-prefixes="xsi xs user ser">
	<xsl:output method="xml" encoding="UTF-8" indent="yes" omit-xml-declaration="no"/>
	<xsl:param name="provisionEntityName"/>
	<xsl:param name="provisionEntityId"/>
	<xsl:param name="submittedDate"/>
	<xsl:template match="/">
		<xsl:choose>
			<xsl:when test="$provisionEntityName = 'user' ">
				<xsl:call-template name="userTemplate"/>
			</xsl:when>
			<xsl:when test="$provisionEntityName = 'customer' ">
				<xsl:call-template name="customerTemplate"/>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	<xsl:template name="userTemplate">
		<user>
			<id>
				<xsl:value-of select="$provisionEntityId"/>
			</id>
			<xsl:call-template name="provisionResponseTemplate"/>
		</user>
	</xsl:template>
	<xsl:template name="customerTemplate">
		<customer>
			<id>
				<xsl:value-of select="$provisionEntityId"/>
			</id>
			<partyDetail>
				<xsl:call-template name="provisionResponseTemplate"/>
			</partyDetail>
		</customer>
	</xsl:template>
	<xsl:template name="provisionResponseTemplate">
			<xsl:if test="ser:CreateUser/ser:Response/ser:SuccessMessage !=''">
				<createdInOpenAM>
					<key>YES_OR_NO_YES</key>
				</createdInOpenAM>
				<createdInOpenAMDttm><xsl:value-of select="$submittedDate"/></createdInOpenAMDttm>
			</xsl:if>
			<xsl:if test="ser:CreateUser/ser:Response/ser:FailureMessage !=''">
				<createdInOpenAM>
					<key>YES_OR_NO_NO</key>
				</createdInOpenAM>
				<createdInOpenAMMessage>
					<xsl:value-of select="ser:CreateUser/ser:Response/ser:FailureMessage"/>
				</createdInOpenAMMessage>
			</xsl:if>
	</xsl:template>
</xsl:stylesheet>
