<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" exclude-result-prefixes="xsl"  xmlns:moody="http://schemas.thirdpillar.com/MoodyFinancial">
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
	<xsl:template match="moody:MoodysGetFinancialData">
		<xsl:element name="moodyCustomer">
			<xsl:apply-templates select="@*|node()"/>
		</xsl:element>
	</xsl:template>	
	<xsl:template match="moody:Response">
		<xsl:apply-templates select="@*|node()"/>
	</xsl:template>	
	<xsl:template match="moody:FinancialStatements">
		<xsl:element name="financialStatements">
			<xsl:apply-templates select="@*|node()"/>
		</xsl:element>
	</xsl:template>
	<xsl:template match="moody:GeneralIndustriesModel">
		<xsl:element name="generalIndustryModel">
			<xsl:apply-templates select="@*|node()"/>
		</xsl:element>
	</xsl:template>
	
	<xsl:template match="moody:FinancialStatement">
		<xsl:element name="moodyFinancialStatement">
			<xsl:apply-templates select="@*|node()"/>
			<xsl:element name="moodyCustomer">
				<xsl:attribute name="reference"><xsl:value-of select="'../../..'"/></xsl:attribute>
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
	<xsl:template match="moody:COGSDepreciation">
		<xsl:element name="cogsDepreciation">
			<xsl:apply-templates select="@*|node()"/>
		</xsl:element>
	</xsl:template>
	<xsl:template match="moody:SGAExpense">
		<xsl:element name="sgaExpense">
			<xsl:apply-templates select="@*|node()"/>
		</xsl:element>
	</xsl:template>
	<xsl:template match="moody:LIFOReserve">
		<xsl:element name="lifoReserve">
			<xsl:apply-templates select="@*|node()"/>
		</xsl:element>
	</xsl:template>
	<xsl:template match="moody:EBITDA">
		<xsl:element name="ebitda">
			<xsl:apply-templates select="@*|node()"/>
		</xsl:element>
	</xsl:template>
	<xsl:template match="moody:EBITDAToIntCPLTD">
		<xsl:element name="ebitdaToIntCPLTD">
			<xsl:apply-templates select="@*|node()"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="CamelCaseWord">		
		  <xsl:param name="text"/>
		  <xsl:value-of select="translate(substring($text,1,1),$vUpper,$vLower)" /><xsl:value-of select="substring($text,2,string-length($text)-1)" />
	</xsl:template>	
	
</xsl:stylesheet>
