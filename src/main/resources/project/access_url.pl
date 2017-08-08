use ElectricCommander;

my $ec = new ElectricCommander();
my $code = $ec->getProperty("/myProject/WebAccessDriver")->findvalue("//value")->value();

eval($code) or die("Error loading WebAccess library code: $@\n");

my $url              = ($ec->getProperty( "url" ))->findvalue('//value')->string_value;
my $getHtml          = ($ec->getProperty( "getHtml" ))->findvalue('//value')->string_value;
my $checkCredentials = ($ec->getProperty( "checkCredentials" ))->findvalue('//value')->string_value;
my $username         = ($ec->getProperty( "username" ))->findvalue('//value')->string_value;
my $password         = ($ec->getProperty( "password" ))->findvalue('//value')->string_value;
my $checkTime        = ($ec->getProperty( "checkTime" ))->findvalue('//value')->string_value;
my $html_outpp        = ($ec->getProperty( "html_outpp" ))->findvalue('//value')->string_value;

plugin_info();
access_url($url,$getHtml,$checkCredentials,$username,$password,$checkTime,$html_outpp);