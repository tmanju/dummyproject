<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:sig="http://citadel.thirdpillar.com/service/signing">
            <xsl:output cdata-section-elements="document" encoding="UTF-8" method="xml" />
            <xsl:template match="/">
                        <xsl:value-of select="//sig:initializeDocumentResponse/document" disable-output-escaping="yes"/>
            </xsl:template>
</xsl:stylesheet>
