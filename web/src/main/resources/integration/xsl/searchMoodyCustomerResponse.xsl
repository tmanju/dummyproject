<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" exclude-result-prefixes="xsl"  xmlns:moody="http://schemas.thirdpillar.com/MoodyCustomer">
	<xsl:output method="xml" encoding="UTF-8" indent="yes" omit-xml-declaration="no"/>
	<xsl:variable name="vLower" select="'abcdefgijklmnopqrstuvwxyz'"/>
	<xsl:variable name="vUpper" select="'ABCDEFGIJKLMNOPQRSTUVWXYZ'"/>
	<xsl:template match="/|comment()|processing-instruction()">
		<xsl:copy>
			<xsl:apply-templates/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="*">
		<xsl:variable name="camelCaseName">
			<xsl:call-template name="CamelCaseWord">
				<xsl:with-param name="text"  select="local-name()"/>
			</xsl:call-template>
		</xsl:variable>
		<xsl:element name="{$camelCaseName}">
			<xsl:apply-templates select="@*|node()"/>
		</xsl:element>
	</xsl:template>
	<xsl:template match="@*">
		<xsl:attribute name="{local-name()}"><xsl:value-of select="."/></xsl:attribute>
	</xsl:template>
	<xsl:template match="moody:MoodysCustomerSearch">
		<xsl:apply-templates select="@*|node()"/>
	</xsl:template>	
	<xsl:template match="moody:Response">
		<xsl:apply-templates select="@*|node()"/>
	</xsl:template>	
	<xsl:template match="moody:City">
	</xsl:template>	
	<xsl:template match="moody:State">
	</xsl:template>	
	<xsl:template match="moody:ZipCode">
	</xsl:template>	
	<xsl:template match="moody:MoodysCustomers">
		<xsl:element name="list">
			<xsl:apply-templates select="@*|node()"/>
		</xsl:element>
	</xsl:template>
	<xsl:template match="moody:MoodysCustomer">
		<xsl:element name="moodyCustomer">
			<xsl:apply-templates select="@*|node()"/>
			<xsl:element name="address">
				<xsl:element name="city">
					<xsl:value-of select="moody:City"/>
				</xsl:element>
				<xsl:element name="state">
					<xsl:value-of select="moody:State"/>
				</xsl:element>
				<xsl:element name="postalCode">
					<xsl:value-of select="moody:ZipCode"/>
				</xsl:element>
			</xsl:element>
		</xsl:element>
	</xsl:template>
	<xsl:template match="moody:MFAID">
		<xsl:element name="mfaId">
			<xsl:apply-templates select="@*|node()"/>
		</xsl:element>
	</xsl:template>
	<xsl:template match="moody:GFCID">
		<xsl:element name="gfcId">
			<xsl:apply-templates select="@*|node()"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="CamelCaseWord">		
		  <xsl:param name="text"/>
		  <xsl:value-of select="translate(substring($text,1,1),$vUpper,$vLower)" /><xsl:value-of select="substring($text,2,string-length($text)-1)" />
	</xsl:template>	
	
</xsl:stylesheet>
