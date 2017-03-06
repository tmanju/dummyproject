<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format"  xmlns:cit="http://citadel.thirdpillar.com/signing/types" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:sig="http://citadel.thirdpillar.com/service/signing" exclude-result-prefixes="xsl xsi xs sig cit">
	<xsl:output method="xml" encoding="UTF-8" indent="yes"/>
	<xsl:template match="/">
		<xsl:apply-templates select="/document"/>
	
	</xsl:template>
	<xsl:template name="escapedEsignTemplateCDATA">
		<xsl:value-of select="/document/esignTemplate/node" disable-output-escaping="no"/>
	</xsl:template>
	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()"/>
		</xsl:copy>
	</xsl:template>
</xsl:stylesheet>
