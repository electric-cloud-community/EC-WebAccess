
# Data that drives the create step picker registration for this plugin.
my %AccessURL = (
    label       => "WebAccess - AccessURL",
    procedure   => "AccessURL",
    description => "Access a given URL",
    category    => "Utility"
);

my %HTMLtextSearch = (
    label       => "WebAccess - HTMLtextSearch",
    procedure   => "HTMLtextSearch",
    description => "Options of text search on HTML document",
    category    => "Utility"
);

my %RestRequest = (
    label       => "WebAccess - RestRequest",
    procedure   => "RestRequest",
    description => "Makes a REST API Call",
    category    => "Utility"
);

$batch->deleteProperty("/server/ec_customEditors/pickerStep/WebAccess - AccessURL");
$batch->deleteProperty("/server/ec_customEditors/pickerStep/WebAccess - HTMLtextSearch");
$batch->deleteProperty("/server/ec_customEditors/pickerStep/WebAccess - RestRequest");

@::createStepPickerSteps = (\%AccessURL,\%HTMLtextSearch, \%RestRequest);
