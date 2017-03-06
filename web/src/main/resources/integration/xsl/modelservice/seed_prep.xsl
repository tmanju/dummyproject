<?xml version="1.0" encoding="UTF-8"?>
<!-- Documentation for using Identity templates can be found at: http://www.xmlplease.com/xsltidentity -->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xs="http://www.w3.org/2001/XMLSchema">
	<xsl:output method="xml" encoding="UTF-8" indent="yes"/>
	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="id">
	</xsl:template>
	<xsl:template match="metaInfo">
	</xsl:template>
	<xsl:template match="version">
	</xsl:template>
	<xsl:template match="sr.getEntityResponse">
		<xsl:apply-templates select="@*|node()"/>
	</xsl:template>
	</xsl:stylesheet>
