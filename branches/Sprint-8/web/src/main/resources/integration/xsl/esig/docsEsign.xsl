<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xs="http://www.w3.org/2001/XMLSchema">
	<xsl:output method="xml" encoding="UTF-8" indent="yes"/>
	<xsl:template match="/">
		<list>
			<xsl:call-template name="FillDocument">
				<xsl:with-param name="request" select="/list/request"/>
				<xsl:with-param name="documents" select="/list/list/document" />
			</xsl:call-template>
		</list>
	</xsl:template>
	<xsl:template name="FillDocument">
		<xsl:param name="request"/>
		<xsl:param name="documents"/>
			<xsl:for-each select="$documents">
						<document>
							<xsl:if test="id">
							<id>
								<xsl:value-of select="id"/>
							</id>
							</xsl:if>
							<xsl:if test="numIndex">
							<numIndex>
								<xsl:value-of select="numIndex"/>
							</numIndex>
							</xsl:if>
							<esignTemplate>
								&lt;document&gt;
										&lt;id&gt;
											<xsl:value-of select="id"/>
												&lt;/id&gt;
												&lt;name&gt;
											<xsl:value-of select="documentName"/>
												&lt;/name&gt; 	
												&lt;extension>pdf&lt;/extension&gt;
												&lt;metadata&gt;
									<xsl:for-each select="$request/relationship/allRelationshipParties/relationshipParty[not(customer/id=preceding-sibling::relationshipParty/customer/id) and customer/partyDetail[partyDetailType/key = 'CUSTOMER_TYPE_INDIVIDUAL'] ]">
										<xsl:call-template name="StampTemplate">
											<xsl:with-param name="stampTemp" select="customer/partyDetail[partyDetailType/key = 'CUSTOMER_TYPE_INDIVIDUAL']"/>
										</xsl:call-template>	
								</xsl:for-each>
								<xsl:for-each select="$request/allFacilities/facility/funding/funder">
									<xsl:call-template name="StampTemplate">
										<xsl:with-param name="stampTemp" select="."/>
									</xsl:call-template>
								</xsl:for-each>
									&lt;/metadata&gt;
									&lt;/document&gt;
								</esignTemplate>
							
						</document>
					</xsl:for-each>
	</xsl:template>
	<xsl:template name="StampTemplate">
		<xsl:param name="stampTemp"/>
								&lt;stamp&gt;
								&lt;id&gt;
					<xsl:text>in-</xsl:text>
		<xsl:value-of select="$stampTemp/firstName"/>
		<xsl:value-of select="$stampTemp/lastName"/>
&lt;/id&gt;
&lt;type&gt;
					<xsl:text>Initial</xsl:text>
&lt;/type&gt;
&lt;description&gt;
					<xsl:text>Initialled by -</xsl:text>
		<xsl:value-of select="$stampTemp/firstName"/>
		<xsl:text> </xsl:text>
		<xsl:value-of select="$stampTemp/lastName"/>
&lt;/description&gt;
&lt;userId&gt;
					<xsl:value-of select="$stampTemp/firstName"/>
		<xsl:value-of select="$stampTemp/lastName"/>
&lt;/userId&gt;
&lt;width&gt;
					<xsl:text>135</xsl:text>
&lt;/width&gt;
&lt;height&gt;
	            	<xsl:text>12</xsl:text>
&lt;/height&gt;
&lt;/stamp&gt;
&lt;stamp&gt;
&lt;id&gt;
					<xsl:text>sn-</xsl:text>
		<xsl:value-of select="$stampTemp/firstName"/>
		<xsl:value-of select="$stampTemp/lastName"/>
&lt;/id&gt;
&lt;type&gt;
					<xsl:text>Signature</xsl:text>
&lt;/type&gt;
&lt;description&gt;
					<xsl:text>Signed by -</xsl:text>
		<xsl:value-of select="$stampTemp/firstName"/>
		<xsl:text> </xsl:text>
		<xsl:value-of select="$stampTemp/lastName"/>
&lt;/description&gt;
&lt;userId&gt;
					<xsl:value-of select="$stampTemp/firstName"/>
		<xsl:value-of select="$stampTemp/lastName"/>
&lt;/userId&gt;
&lt;width&gt;
					<xsl:text>135</xsl:text>
&lt;/width&gt;
&lt;height&gt;
	            	<xsl:text>12</xsl:text>
&lt;/height&gt;
&lt;/stamp&gt;
&lt;stamp&gt;
&lt;id&gt;
					<xsl:text>dt-</xsl:text>
		<xsl:value-of select="$stampTemp/firstName"/>
		<xsl:value-of select="$stampTemp/lastName"/>
&lt;/id&gt;
&lt;type&gt;
					<xsl:text>Date</xsl:text>
&lt;/type&gt;
&lt;userId&gt;
					<xsl:value-of select="$stampTemp/firstName"/>
		<xsl:value-of select="$stampTemp/lastName"/>
&lt;/userId&gt;
&lt;width&gt;
					<xsl:text>135</xsl:text>
&lt;/width&gt;
&lt;height&gt;
	            	<xsl:text>12</xsl:text>
&lt;/height&gt;
&lt;/stamp&gt;
&lt;stamp&gt;
&lt;id&gt;
					<xsl:text>nm-</xsl:text>
		<xsl:value-of select="$stampTemp/firstName"/>
		<xsl:value-of select="$stampTemp/lastName"/>
&lt;/id&gt;
&lt;type&gt;
					<xsl:text>Name</xsl:text>
&lt;/type&gt;
&lt;description&gt;
					<xsl:text>Name printed by -</xsl:text>
		<xsl:value-of select="$stampTemp/firstName"/>
		<xsl:text> </xsl:text>
		<xsl:value-of select="$stampTemp/lastName"/>
&lt;/description&gt;
&lt;userId&gt;
					<xsl:value-of select="$stampTemp/firstName"/>
		<xsl:value-of select="$stampTemp/lastName"/>
&lt;/userId&gt;
&lt;width&gt;
					<xsl:text>135</xsl:text>
&lt;/width&gt;
&lt;height&gt;
	            	<xsl:text>12</xsl:text>
&lt;/height&gt;
&lt;/stamp&gt;
	</xsl:template>
</xsl:stylesheet>
