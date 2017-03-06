<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" exclude-result-prefixes="xsl">
	<xsl:output method="xml" encoding="UTF-8" indent="yes" omit-xml-declaration="no"/>
	<xsl:template match="/|comment()|processing-instruction()">
		<xsl:copy>
			<xsl:apply-templates/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="*">
		<xsl:element name="{local-name()}">
			<xsl:apply-templates select="@*|node()"/>
		</xsl:element>
	</xsl:template>
	<xsl:template match="list">
		<xsl:element name="{local-name()}">
			<xsl:attribute name="type"><xsl:value-of select=" 'array' "/></xsl:attribute>
			<xsl:apply-templates select="@*|node()"/>
		</xsl:element>
	</xsl:template>
	<xsl:template match="allDocuments">
		<xsl:element name="{local-name()}">
			<xsl:attribute name="type"><xsl:value-of select=" 'array' "/></xsl:attribute>
			<xsl:apply-templates select="@*|node()"/>
		</xsl:element>
	</xsl:template>
	<xsl:template match="document">
		<xsl:element name="{local-name()}">
			<xsl:attribute name="type"><xsl:value-of select=" 'array' "/></xsl:attribute>
			<xsl:apply-templates select="@*|node()"/>
		</xsl:element>
	</xsl:template>
	<xsl:template match="generatedDocuments">
		<xsl:element name="{local-name()}">
			<xsl:attribute name="type"><xsl:value-of select=" 'array' "/></xsl:attribute>
			<xsl:apply-templates select="@*|node()"/>
		</xsl:element>
	</xsl:template>
	<xsl:template match="uploadedDocuments">
		<xsl:element name="{local-name()}">
			<xsl:attribute name="type"><xsl:value-of select=" 'array' "/></xsl:attribute>
			<xsl:apply-templates select="@*|node()"/>
		</xsl:element>
	</xsl:template>
	<xsl:template match="faqs">
		<xsl:element name="{local-name()}">
			<xsl:attribute name="type"><xsl:value-of select=" 'array' "/></xsl:attribute>
			<xsl:apply-templates select="@*|node()"/>
		</xsl:element>
	</xsl:template>
	<xsl:template match="invitations">
		<xsl:element name="{local-name()}">
			<xsl:attribute name="type"><xsl:value-of select=" 'array' "/></xsl:attribute>
			<xsl:apply-templates select="@*|node()"/>
		</xsl:element>
	</xsl:template>
	<!-- populate document binary only when individual document data is pulled -->
	<xsl:template match="uploadedStream">
		<xsl:variable name="rootElementName" select="name(/*)"/>
		<xsl:if test="$rootElementName = 'Document' or $rootElementName = 'document' ">
			<xsl:element name="{local-name()}">
				<xsl:apply-templates select="@*|node()"/>
			</xsl:element>
		</xsl:if>
	</xsl:template>
	<xsl:template match="@*">
		<xsl:attribute name="{local-name()}"><xsl:value-of select="."/></xsl:attribute>
	</xsl:template>
</xsl:stylesheet>
