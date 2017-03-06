<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:cit="http://citadel.thirdpillar.com/signing/types" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:sig="http://citadel.thirdpillar.com/service/signing" exclude-result-prefixes="xsl xsi xs sig cit">
	<xsl:output method="xml" encoding="UTF-8" indent="yes"/>
	<xsl:template match="/">
		<document>
			<xsl:call-template name="extractDocData">
				<xsl:with-param name="docRoot" select="/cit:document"/>
			</xsl:call-template>
		</document>
	</xsl:template>
	<xsl:template name="extractDocData">
		<xsl:param name="docRoot"/>
		<id>
			<xsl:value-of select="$docRoot/cit:id"/>
		</id>
		<documentName>
			<xsl:value-of select="$docRoot/cit:name"/>
			<xsl:text>.</xsl:text>
			<xsl:value-of select="$docRoot/cit:extension"/>
		</documentName>
		<uploadedStream>
			<xsl:value-of select="$docRoot/cit:content"/>
		</uploadedStream>
		<esignTemplate>
			<xsl:text disable-output-escaping="yes">&lt;![CDATA[</xsl:text>
			<document>
				<id>
					<xsl:value-of select="$docRoot/cit:id"/>
				</id>
				<name>
					<xsl:value-of select="$docRoot/cit:name"/>
					<xsl:text>.</xsl:text>
					<xsl:value-of select="$docRoot/cit:extension"/>
				</name>
				<extension><xsl:value-of select="$docRoot/cit:extension"/></extension>
				<xsl:copy-of select="$docRoot/cit:metadata"/>
			</document>
			<xsl:text disable-output-escaping="yes">]]</xsl:text>
			<xsl:text disable-output-escaping="yes">&gt;</xsl:text>
		</esignTemplate>
		<initializeDttm><xsl:value-of select="$docRoot/cit:metadata/cit:initializeDttm"/></initializeDttm>
		<xsl:variable name="docSigner" select="$docRoot/cit:metadata/cit:documentSigner"/>
		<documentSigners>
			<xsl:for-each select="$docSigner">
				<documentSigner>
					<signerId>
						<xsl:value-of select="cit:id"/>
					</signerId>
					<name>
						<xsl:value-of select="cit:name"/>
					</name>
					<signerType>
						<xsl:value-of select="cit:signerType"/>
					</signerType>
					<signerRefId>
						<xsl:value-of select="cit:externalRefId"/>
					</signerRefId>
					<document reference="../../.."/>
				</documentSigner>
			</xsl:for-each>
		</documentSigners>
		<xsl:if test="boolean($docRoot/cit:metadata/cit:imageParams/cit:prefetchImages)">
		<documentPagesAsImages>
			<xsl:variable name="pageAsImage" select="$docRoot/cit:pageAsImage"/>
			<xsl:for-each select="$pageAsImage">
				<pageAsImage>
					<page><xsl:value-of select="cit:page"/></page>
					<image><xsl:value-of select="cit:image"/></image>
					<imageWidth><xsl:value-of select="cit:imageWidth"/></imageWidth>
					<imageHeight><xsl:value-of select="cit:imageHeight"/></imageHeight>
					<document reference="../../.."/>
				</pageAsImage>
			</xsl:for-each>
		</documentPagesAsImages>
		</xsl:if>
	</xsl:template>
</xsl:stylesheet>
