<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:template match="person">
        <person>
            <xsl:for-each select="*[name()!='document']">
                <xsl:attribute name="{name()}">
                    <xsl:value-of select="text()"/>
                </xsl:attribute>
            </xsl:for-each>
            <xsl:for-each select="document">
                <document>
                    <xsl:attribute name="series">
                        <xsl:value-of select="series"/>
                    </xsl:attribute>
                    <xsl:attribute name="number">
                        <xsl:value-of select="number"/>
                    </xsl:attribute>
                    <xsl:attribute name="type">
                        <xsl:value-of select="type"/>
                    </xsl:attribute>
                    <xsl:attribute name="issueDate">
                        <xsl:value-of select="issueDate"/>
                    </xsl:attribute>
                </document>
            </xsl:for-each>
        </person>
    </xsl:template>

</xsl:stylesheet>