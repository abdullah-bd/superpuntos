package com.iotait.superpuntos.helper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VersionCk {

    public static boolean isVersionHigher(String baseVersion, String testVersion)
    {
        System.out.println( "versionToComparable( baseVersion ) =" + versionToComparable( baseVersion ) );
        System.out.println( "versionToComparable( testVersion ) =" + versionToComparable( testVersion ) + " is this higher ?" );
        return versionToComparable( testVersion ).compareTo( versionToComparable( baseVersion ) ) > 0;
    }

    //----  not worrying about += for something so small
    private static String versionToComparable( String version )
    {
//        System.out.println("version - " + version);
        String versionNum = version;
        int at = version.indexOf( '-' );
        if ( at >= 0 )
            versionNum = version.substring( 0, at );

        String[] numAr = versionNum.split( "\\." );
        String versionFormatted = "0";
        for ( String tmp : numAr )
        {
            versionFormatted += String.format( "%4s", tmp ).replace(' ', '0');
        }
        while ( versionFormatted.length() < 12 )  // pad out to aaaa.bbbb.cccc
        {
            versionFormatted += "0000";
        }
//        System.out.println( "converted min version =" + versionFormatted + "=   : " + versionNum );
        return versionFormatted + getVersionModifier( version, at );
    }

    //----  use order low to high: -xyz, -SNAPSHOT, -ALPHA, -BETA, -RC, -RELEASE/nothing  returns: 0, 1, 2, 3, 4, 5
    private static String getVersionModifier( String version, int at )
    {
//        System.out.println("version - " + version );
        String[] wordModsAr = { "-SNAPSHOT", "-ALPHA", "-BETA", "-RC", "-RELEASE" };

        if ( at < 0 )
            return "." + wordModsAr.length + "00";   // make nothing = RELEASE level

        int i = 1;
        for ( String word : wordModsAr )
        {
            if ( ( at = version.toUpperCase().indexOf( word ) ) > 0 )
                return "." + i + getSecondVersionModifier( version.substring( at + word.length() ) );
            i++;
        }

        return ".000";
    }

    //----  add 2 chars for any number after first modifier.  -rc2 or -rc-2   returns 02
    private static String getSecondVersionModifier( String version )
    {
        System.out.println( "second modifier =" + version + "=" );
        Matcher m = Pattern.compile("(.*?)(\\d+).*").matcher( version );
//        if ( m.matches() )
//            System.out.println( "match ? =" + m.matches() + "=   m.group(1) =" + m.group(1) + "=   m.group(2) =" + m.group(2) + "=   m.group(3) =" + (m.groupCount() >= 3 ? m.group(3) : "x") );
//        else
//            System.out.println( "No match" );
        return m.matches() ? String.format( "%2s", m.group(2) ).replace(' ', '0') : "00";
    }

}