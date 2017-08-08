use ElectricCommander;

my $ec = new ElectricCommander();
my $code = $ec->getProperty("/myProject/WebAccessDriver")->findvalue("//value")->value();

eval($code) or die("Error loading WebAccess library code: $@\n");

my $baseUrl          = ($ec->getProperty( "baseUrl" ))->findvalue('//value')->string_value;    
my $pathUrl          = ($ec->getProperty( "pathUrl" ))->findvalue('//value')->string_value;    
my $port             = ($ec->getProperty( "port" ))->findvalue('//value')->string_value;    
my $contentType      = ($ec->getProperty( "contentType" ))->findvalue('//value')->string_value;
my $formContent      = ($ec->getProperty( "formContent" ))->findvalue('//value')->string_value;
my $headers          = ($ec->getProperty( "headers" ))->findvalue('//value')->string_value;
my $username         = ($ec->getProperty( "username" ))->findvalue('//value')->string_value;
my $password         = ($ec->getProperty( "password" ))->findvalue('//value')->string_value;
my $requestType      = ($ec->getProperty( "requestType" ))->findvalue('//value')->string_value;
my $authentication   = ($ec->getProperty( "authentication" ))->findvalue('//value')->string_value;
my $response_outpp   = ($ec->getProperty( "response_outpp" ))->findvalue('//value')->string_value;

plugin_info();
rest_request($baseUrl, $pathUrl, $port, $contentType, $formContent, $username,$password, $requestType, $authentication,$headers,$response_outpp);