use ElectricCommander;

my $ec = new ElectricCommander();
my $code = $ec->getProperty("/myProject/WebAccessDriver")->findvalue("//value")->value();

eval($code) or die("Error loading WebAccess library code: $@\n");

my $searchOp         = ($ec->getProperty( "searchOp" ))->findvalue('//value')->string_value;    
my $local            = ($ec->getProperty( "local" ))->findvalue('//value')->string_value;    
my $url              = ($ec->getProperty( "url" ))->findvalue('//value')->string_value;    
my $htmlText         = ($ec->getProperty( "htmlText" ))->findvalue('//value')->string_value;
my $searchType       = ($ec->getProperty( "searchType" ))->findvalue('//value')->string_value;
my $text             = ($ec->getProperty( "text" ))->findvalue('//value')->string_value;
my $tag              = ($ec->getProperty( "tag" ))->findvalue('//value')->string_value;

plugin_info();
html_text_search($searchOp,$local,$url,$htmlText,$searchType,$text,$tag);