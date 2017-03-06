<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:sig="http://citadel.thirdpillar.com/service/signing" exclude-result-prefixes="xsl xsi xs sig">
	<xsl:output method="xml" encoding="UTF-8" indent="yes"/>
	<xsl:template match="/">
		<sig:initializeDocument>
			<document>
				<xsl:variable name="docCDATAFirstLine" select="concat('&lt;![CDATA[','&lt;document xsi:schemaLocation=&quot;http://citadel.thirdpillar.com/signing/types ../../../main/resources/xsd/document-signing.xsd&quot; xmlns:xsi=&quot;http://www.w3.org/2001/XMLSchema-instance&quot; xmlns=&quot;http://citadel.thirdpillar.com/signing/types&quot;&gt;')"/>
				<xsl:value-of select="$docCDATAFirstLine" disable-output-escaping="yes"/>
				<xsl:call-template name="docCDATA"/>
				<xsl:value-of select=" '&lt;/document&gt;]]>' " disable-output-escaping="yes"/>
			</document>
		</sig:initializeDocument>
	</xsl:template>
	<xsl:template name="docCDATA">
		<xsl:call-template name="documentData"/>
	</xsl:template>
	<xsl:template name="documentData">
		<xsl:variable name="request" select="/list/request"/>
		<xsl:variable name="doc" select="/list/list/document"/>
		<xsl:variable name="filename" select="$doc/documentName"/>
		<id>
			<xsl:value-of select="$doc/id"/>
		</id>
		<name>
			<xsl:value-of select="substring-before($filename, '.')"/>
		</name>
		<extension>
			<xsl:value-of select="substring-after($filename, '.')"/>
		</extension>
		<content>
			<xsl:value-of select="$doc/uploadedStream"/>
		</content>
		<metadata>
			<xsl:for-each select="$request/relationship/allRelationshipParties/relationshipParty[not(customer/id=preceding-sibling::relationshipParty/customer/id) and customer/customerType[key = 'CUSTOMER_TYPE_INDIVIDUAL'] ]">
				<xsl:call-template name="StampTemplate">
					<xsl:with-param name="user" select="customer/customerType[key = 'CUSTOMER_TYPE_INDIVIDUAL']"/>
					<xsl:with-param name="userType">
						<xsl:text>PARTY</xsl:text>
					</xsl:with-param>
				</xsl:call-template>
			</xsl:for-each>
			<xsl:for-each select="$request/allFacilities/facility/facilityAddDoc/lenderOfficer">
				<xsl:call-template name="StampTemplate">
					<xsl:with-param name="user" select="."/>
					<xsl:with-param name="userType">
						<xsl:text>LENDER</xsl:text>
					</xsl:with-param>
				</xsl:call-template>
			</xsl:for-each>
			<!-- Special Case for Whitney Branstetter -->
			<xsl:variable name="wbUsername" select="'wbranstetter'"/>
			<xsl:variable name="wbExtRefId" select="'NBKOV9L'"/>
			<xsl:variable name="wbFirstname" select="'Whitney'"/>
			<xsl:variable name="wbMiddlename" select="''"/>
			<xsl:variable name="wbLastname" select="'Branstetter'"/>
			<stamp>
				<id>
					<xsl:text>in-</xsl:text>
					<xsl:value-of select="$wbExtRefId"/>
				</id>
				<type>
					<xsl:text>Initial</xsl:text>
				</type>
				<description>
					<xsl:text>Initialed by </xsl:text>
					<xsl:value-of select="$wbFirstname"/>
					<xsl:if test="boolean($wbMiddlename)">
					<xsl:text> </xsl:text>
					<xsl:value-of select="$wbMiddlename"/>
					</xsl:if>
					<xsl:text> </xsl:text>
					<xsl:value-of select="$wbLastname"/>
				</description>
				<firstName>
					<xsl:value-of select="$wbFirstname"/>
				</firstName>
				<middleName>
					<xsl:value-of select="$wbMiddlename"/>
				</middleName>
				<lastName>
					<xsl:value-of select="$wbLastname"/>
				</lastName>
				<userType>
					<xsl:text>SIGNER_TYPE_USER</xsl:text>
				</userType>
				<userId>
					<xsl:value-of select="$wbExtRefId"/>
				</userId>
				<xsl:call-template name="InitialsStampDimensionsTemplate"/>
			</stamp>
			<stamp>
				<id>
					<xsl:text>sn-</xsl:text>
					<xsl:value-of select="$wbExtRefId"/>
				</id>
				<type>
					<xsl:text>Signature</xsl:text>
				</type>
				<description>
					<xsl:text>Signed by </xsl:text>
					<xsl:value-of select="$wbFirstname"/>
					<xsl:if test="boolean($wbMiddlename)">
					<xsl:text> </xsl:text>
					<xsl:value-of select="$wbMiddlename"/>
					</xsl:if>
					<xsl:text> </xsl:text>
					<xsl:value-of select="$wbLastname"/>
				</description>
				<firstName>
					<xsl:value-of select="$wbFirstname"/>
				</firstName>
				<middleName>
					<xsl:value-of select="$wbMiddlename"/>
				</middleName>
				<lastName>
					<xsl:value-of select="$wbLastname"/>
				</lastName>
				<userType>
					<xsl:text>SIGNER_TYPE_USER</xsl:text>
				</userType>
				<userId>
					<xsl:value-of select="$wbExtRefId"/>
				</userId>
				<xsl:call-template name="SignatureStampDimensionsTemplate"/>
			</stamp>
			<stamp>
				<id>
					<xsl:text>dt-</xsl:text>
					<xsl:value-of select="$wbExtRefId"/>
				</id>
				<type>
					<xsl:text>Date</xsl:text>
				</type>
				<firstName>
					<xsl:value-of select="$wbFirstname"/>
				</firstName>
				<middleName>
					<xsl:value-of select="$wbMiddlename"/>
				</middleName>
				<lastName>
					<xsl:value-of select="$wbLastname"/>
				</lastName>
				<userType>
					<xsl:text>SIGNER_TYPE_USER</xsl:text>
				</userType>
				<userId>
					<xsl:value-of select="$wbExtRefId"/>
				</userId>
				<xsl:call-template name="DateStampDimensionsTemplate"/>
			</stamp>
			<stamp>
				<id>
					<xsl:text>nm-</xsl:text>
					<xsl:value-of select="$wbExtRefId"/>
				</id>
				<type>
					<xsl:text>Name</xsl:text>
				</type>
				<description>
					<xsl:text>Name printed by </xsl:text>
					<xsl:value-of select="$wbFirstname"/>
					<xsl:if test="boolean($wbMiddlename)">
					<xsl:text> </xsl:text>
					<xsl:value-of select="$wbMiddlename"/>
					</xsl:if>
					<xsl:text> </xsl:text>
					<xsl:value-of select="$wbLastname"/>
				</description>
				<firstName>
					<xsl:value-of select="$wbFirstname"/>
				</firstName>
				<middleName>
					<xsl:value-of select="$wbMiddlename"/>
				</middleName>
				<lastName>
					<xsl:value-of select="$wbLastname"/>
				</lastName>
				<userType>
					<xsl:text>SIGNER_TYPE_USER</xsl:text>
				</userType>
				<userId>
					<xsl:value-of select="$wbExtRefId"/>
				</userId>
				<xsl:call-template name="NameStampDimensionsTemplate"/>
			</stamp>
			<!-- END special case for Whitney Branstetter -->
			<imageParams>
				<prefetchImages>true</prefetchImages>
				<scalingFactor>1.5</scalingFactor>
			</imageParams>
		</metadata>
	</xsl:template>
	<xsl:template name="StampTemplate">
		<xsl:param name="user"/>
		<xsl:param name="userType"/>
		<stamp>
			<id>
				<xsl:text>in-</xsl:text>
				<xsl:call-template name="StampIDTemplate">
					<xsl:with-param name="user" select="$user"/>
					<xsl:with-param name="userType" select="$userType"/>
				</xsl:call-template>
			</id>
			<type>
				<xsl:text>Initial</xsl:text>
			</type>
			<description>
				<xsl:text>Initialed by </xsl:text>
				<xsl:call-template name="FullNameTemplate">
					<xsl:with-param name="user" select="$user"/>
					<xsl:with-param name="userType" select="$userType"/>
				</xsl:call-template>
			</description>
			<xsl:call-template name="NameTemplate">
				<xsl:with-param name="user" select="$user"/>
				<xsl:with-param name="userType" select="$userType"/>
			</xsl:call-template>
			<userType>
				<xsl:call-template name="UserTypeTemplate">
					<xsl:with-param name="userType" select="$userType"/>
				</xsl:call-template>
			</userType>
			<userId>
				<xsl:call-template name="StampIDTemplate">
					<xsl:with-param name="user" select="$user"/>
					<xsl:with-param name="userType" select="$userType"/>
				</xsl:call-template>
			</userId>
			<xsl:call-template name="InitialsStampDimensionsTemplate"/>
		</stamp>
		<stamp>
			<id>
				<xsl:text>sn-</xsl:text>
				<xsl:call-template name="StampIDTemplate">
					<xsl:with-param name="user" select="$user"/>
					<xsl:with-param name="userType" select="$userType"/>
				</xsl:call-template>
			</id>
			<type>
				<xsl:text>Signature</xsl:text>
			</type>
			<description>
				<xsl:text>Signed by </xsl:text>
				<xsl:call-template name="FullNameTemplate">
					<xsl:with-param name="user" select="$user"/>
					<xsl:with-param name="userType" select="$userType"/>
				</xsl:call-template>
			</description>
			<xsl:call-template name="NameTemplate">
				<xsl:with-param name="user" select="$user"/>
				<xsl:with-param name="userType" select="$userType"/>
			</xsl:call-template>
			<userType>
				<xsl:call-template name="UserTypeTemplate">
					<xsl:with-param name="userType" select="$userType"/>
				</xsl:call-template>
			</userType>
			<userId>
				<xsl:call-template name="StampIDTemplate">
					<xsl:with-param name="user" select="$user"/>
					<xsl:with-param name="userType" select="$userType"/>
				</xsl:call-template>
			</userId>
			<xsl:call-template name="SignatureStampDimensionsTemplate"/>
		</stamp>
		<stamp>
			<id>
				<xsl:text>dt-</xsl:text>
				<xsl:call-template name="StampIDTemplate">
					<xsl:with-param name="user" select="$user"/>
					<xsl:with-param name="userType" select="$userType"/>
				</xsl:call-template>
			</id>
			<type>
				<xsl:text>Date</xsl:text>
			</type>
			<xsl:call-template name="NameTemplate">
				<xsl:with-param name="user" select="$user"/>
				<xsl:with-param name="userType" select="$userType"/>
			</xsl:call-template>
			<userType>
				<xsl:call-template name="UserTypeTemplate">
					<xsl:with-param name="userType" select="$userType"/>
				</xsl:call-template>
			</userType>
			<userId>
				<xsl:call-template name="StampIDTemplate">
					<xsl:with-param name="user" select="$user"/>
					<xsl:with-param name="userType" select="$userType"/>
				</xsl:call-template>
			</userId>
			<xsl:call-template name="DateStampDimensionsTemplate"/>
		</stamp>
		<stamp>
			<id>
				<xsl:text>nm-</xsl:text>
				<xsl:call-template name="StampIDTemplate">
					<xsl:with-param name="user" select="$user"/>
					<xsl:with-param name="userType" select="$userType"/>
				</xsl:call-template>
			</id>
			<type>
				<xsl:text>Name</xsl:text>
			</type>
			<description>
				<xsl:text>Name printed by </xsl:text>
				<xsl:call-template name="FullNameTemplate">
					<xsl:with-param name="user" select="$user"/>
					<xsl:with-param name="userType" select="$userType"/>
				</xsl:call-template>
			</description>
			<xsl:call-template name="NameTemplate">
				<xsl:with-param name="user" select="$user"/>
				<xsl:with-param name="userType" select="$userType"/>
			</xsl:call-template>
			<userType>
				<xsl:call-template name="UserTypeTemplate">
					<xsl:with-param name="userType" select="$userType"/>
				</xsl:call-template>
			</userType>
			<userId>
				<xsl:call-template name="StampIDTemplate">
					<xsl:with-param name="user" select="$user"/>
					<xsl:with-param name="userType" select="$userType"/>
				</xsl:call-template>
			</userId>
			<xsl:call-template name="NameStampDimensionsTemplate"/>
		</stamp>
	</xsl:template>
	<xsl:template name="StampIDTemplate">
		<xsl:param name="user"/>
		<xsl:param name="userType"/>
		<xsl:choose>
			<xsl:when test="$userType='PARTY'">
				<xsl:value-of select="$user/../refNumber"/><!-- customer refNumber -->
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$user/externalRefId"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<xsl:template name="NameTemplate">
		<xsl:param name="user"/>
		<xsl:param name="userType"/>
		<firstName>
			<xsl:value-of select="$user/firstName"/>
		</firstName>
		<middleName>
			<xsl:choose>
				<xsl:when test="$userType = 'PARTY'">
				<xsl:value-of select="$user/middleName"/>
				</xsl:when>
				<xsl:when test="$userType = 'LENDER'">
				<xsl:value-of select="$user/middleInitial"/>
				</xsl:when>
			</xsl:choose>
		</middleName>
		<lastName>
			<xsl:value-of select="$user/lastName"/>
		</lastName>
	</xsl:template>
	<xsl:template name="FullNameTemplate">
		<xsl:param name="user"/>
		<xsl:param name="userType"/>
		<xsl:value-of select="$user/firstName"/>
		<xsl:choose>
			<xsl:when test="$userType = 'PARTY'">
				<xsl:if test="boolean($user/middleName[normalize-space()])">
				<xsl:text> </xsl:text>
				<xsl:value-of select="$user/middleName"/>
				</xsl:if>
			</xsl:when>
			<xsl:when test="$userType = 'LENDER'">
				<xsl:if test="boolean($user/middleInitial[normalize-space()])">
				<xsl:text> </xsl:text>
				<xsl:value-of select="$user/middleInitial"/>
				</xsl:if>
			</xsl:when>
		</xsl:choose>
		<xsl:text> </xsl:text>
		<xsl:value-of select="$user/lastName"/>
	</xsl:template>
	<xsl:template name="UserTypeTemplate">
		<xsl:param name="userType"/>
		<xsl:choose>
			<xsl:when test="$userType='PARTY'">
				<xsl:text>SIGNER_TYPE_PARTY</xsl:text>
			</xsl:when>
			<xsl:otherwise>
				<xsl:text>SIGNER_TYPE_USER</xsl:text>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<xsl:template name="SignatureStampDimensionsTemplate">
		<width>
			<xsl:text>250</xsl:text>
		</width>
		<height>
			<xsl:text>12</xsl:text>
		</height>
	</xsl:template>
	<xsl:template name="NameStampDimensionsTemplate">
		<width>
			<xsl:text>135</xsl:text>
		</width>
		<height>
			<xsl:text>12</xsl:text>
		</height>
	</xsl:template>
	<xsl:template name="DateStampDimensionsTemplate">
		<width>
			<xsl:text>135</xsl:text>
		</width>
		<height>
			<xsl:text>12</xsl:text>
		</height>
	</xsl:template>
	<xsl:template name="InitialsStampDimensionsTemplate">
		<width>
			<xsl:text>50</xsl:text>
		</width>
		<height>
			<xsl:text>12</xsl:text>
		</height>
	</xsl:template>
</xsl:stylesheet>
