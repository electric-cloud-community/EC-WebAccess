########################################################################
# Package
#    WebAccessDriver.pl
#
# Dependencies
#    -none
#
# Purpose
#    Perl script to create a perform basic web access tasks
#
# Date
#    06/08/2012
#
# Engineer
#    Bryan Barrantes
#
# Copyright (c) 2012 Electric Cloud, Inc.
# All rights reserved
########################################################################    
use strict;    
use warnings;
use ElectricCommander;
use ElectricCommander::PropMod;
use LWP 5.825;#5.831;#6.02;
use LWP::Simple;
use JSON;
use Time::HiRes qw( time );
#load module from property
ElectricCommander::PropMod::loadPerlCodeFromProperty($ec,"/myProject/XML::Twig");
local $| = 1;
########################################################################
# access_url - allow to access a url with or wothout credentials,get the html and measure the time elapsed.
#
# Arguments:
#   -url                URL to access.
#   -checkUrl           Check if the URL it's online.
#   -getHtml            Try to get the HTML from the URL.
#   -checkCredentials   If basic authentication is required to access, provide the username and password.
#   -username
#   -password
#   -checkTime          Measure time elapsed.
#   -html_outpp         Allows the user to store the HTML on a property.
#
# Returns:
#   -nothing
#
########################################################################
sub access_url {
    my ($url,$getHtml,$checkCredentials,$username,$password,$checkTime,$html_outpp) = @_;
    
    # Start the Timer
    my $start = time();
    
    # Constants and Vars
    my $browser = LWP::UserAgent->new;
    my $req = HTTP::Request->new(GET => $url);
    my $empty = q{};
    my $response;
    my $separator = qq{----------------------------------------------------------------------------------------------------\n};
    
    # Validate URL
    if($url !~ m/^(https?:\/\/)*([A-Za-z0-9._-]+)(:\d+)*([\/?].*)?$/ixsmg) {
        $ec->setProperty("outcome","error");
        print "Invalid URL: ".$url."\n";
        print "Please check the provided URL.\n"; return;
    }
    
    # Print URL
    print "\nURL: ".$url."\n";
    
    # Check Credentials
    if($checkCredentials == 1) {
        if($username eq $empty or $password eq $empty){            
            $ec->setProperty("outcome","error"); 
            print "\nERROR! One or more credentials provided are empty.\nPlease check your parameters."; 
            return;
        }        
        # Create authorization to server
        $req->authorization_basic($username, $password);        
        
        $response = $browser->request($req);
        print "\n$separator";
        print "Check Credentials:\n";        
        if ($response->is_error) {
            $ec->setProperty("outcome","error");
            if ($response->status_line ne $empty) { print "\nStatus: ".$response->status_line."\n";}
            print "ERROR! The server was unable to fulfil the request with the given credentials.\nThe credentials might be incorrect.";
            return;
        } else {
            print "Checking...\nValid Credentials - Status: ".$response->status_line."\n";
        }
    } else {
        $response = $browser->request($req);
        if ($response->is_error) {
            $ec->setProperty("outcome","error");
            if ($response->status_line ne $empty) { print "\nStatus: ".$response->status_line."\n"; }
            print ("ERROR! The server was unable to fulfil the request on the URL: $url\nPlease check your parameters.");
            return;
        }
    }
    
    # Store HTML in variable to print, if necesary
    my $HTML = $response->content();
    
    # Check URL    
    print "\n$separator";
    print "Check URL:\n";
    print "URL Online - HTTP Status: ".$response->status_line."\n";
    
    # Get HTML    
    if($getHtml == 1) {
        print $separator;        
        print "Get HTML:\n";
        if($HTML) {
            print $HTML;
        } else {
            print "ERROR! The server was unable to return the HTML document.\nPlease check your parameters.\n";
        }
    }
    
    # Store HTML on property
    if($html_outpp ne $empty) { $ec->setProperty($html_outpp,$HTML); }
    
    # Measure time
    my $end = time();
    if($checkTime == 1) {
        print "\n$separator";
        print "Time elapsed: ";
        printf("%.2f\n", $end - $start);
    }
    return;
}

########################################################################
# html_text_search - Search text in an HTML file, or search for a specific tag.
#
# Arguments:
#   -searchOp       Search in:Local File, URL or HTML text.
#   -local          Local file path.
#   -url            URL.
#   -htmlText       HTML text.
#   -searchType     Search for: specific text, or all tags of a kind.
#   -text
#   -tag
#
# Returns:
#   -nothing
#
########################################################################
sub html_text_search {    
    my ($searchOp,$local,$url,$htmlText,$searchType,$text,$tag) = @_;
    
    # Constants and Vars
    my $empty = q{};
    my $size = 0;
    my $HTML = $empty;
    
    # Search Option filter
    if ($searchOp eq "urlOp") {
        my $browser = LWP::UserAgent->new;
        my $req = HTTP::Request->new(GET => $url);
        my $response = $browser->request($req);        
        if ($response->is_error) {
            $ec->setProperty("outcome","error");
            if ($response->status_line ne $empty) { print "\nStatus: ".$response->status_line."\n";}
            print "ERROR! The server was unable to fulfil the request.\nUnable to get the HTML document.";
            return;
        }
        # Store the HTMl on var
        $HTML = $response->content();        
        
        if($searchType eq "textOp") {
            print "Search Option: URL\nSearch Type: Plain Text: '".$text."'\nURL: $url\n";            
            search($text,$HTML);
        } else {
            print "Search Option: Local\nSearch Type: In Tag:'<".$tag.">'\nURL: $url";
            searchTag($tag,$HTML);
        }
    } elsif($searchOp eq "localOp") {
        eval {open FILE, $local or die $!;}; 
        if($!) {            
            print "ERROR! ".$!."\n";
            $ec->setProperty("outcome","error");return;
        }
        my $HTML = do { local $/; <FILE> };
        
        if($searchType eq "textOp") {
            print "Search Option: Local\nSearch Type: Plain Text: '".$text."'\nLocal Path: $local\n";
            search($text,$HTML);
        } else {            
            print "Search Option: Local\nSearch Type: In Tag:'<".$tag.">'\nLocal Path: $local\n";
            searchTag($tag,$HTML);
        }
    } else { #htmlTextOp
        if($searchType eq "textOp") {
            print "Search Option: HTML Text\nSearch Type: Plain Text: '".$text."'\n";
            search($text,$htmlText);
        } else { 
            print "Search Option: Local\nSearch Type: In Tag:'<".$tag.">'\n";
            searchTag($tag,$htmlText);
        }
    }
}
########################################################################
# search - search for plain text in html
#
# Arguments:
#   -search     Text to find
#   -htmlFile   HTML file
#
# Returns:
#   -nothing
#
########################################################################
sub search {
  my ($search, $htmlFile) = @_;
  my $size = 0;
  my @array = split("\n",$htmlFile);
  my $cont = 0;
  my $matches = 0;
  $size = @array;
  while ($size>0) {
    my $line = shift(@array);
    $cont++;
    if ($line =~ m/.*($search).*/sgi) {
        $line =~ s/^(\s*)//g;
        $line =~ s/(\n*)|(\r\n*)//g;        
        print "Match found: $line, at line $cont\n";
        $matches++;
    }
    $size--;
  }
  # If no matches...
  if($matches eq 0){
    $ec->setProperty("outcome","warning");
    print "No matches found.\n";
  } else {
    print "Total matches: $matches\n";
  }
}
########################################################################
# searchTag - searches for tags in the HTML file
#
# Arguments:
#   -tag        Tag name
#   -htmlFile   HTML file
#
# Returns:
#   -nothing
#
########################################################################
sub searchTag {
  my($tag,$htmlFile) = @_;
  
  # Constants and Vars
  my $size = 0;
  my @array = split("\n",$htmlFile);
  my $line = "";
  my $cont = 0;
  my $matches = 0;

  $size = @array;
    while ($size>0) {
        $line = shift(@array); 
        $line =~ s/^(\s*)//g;           # Substitute white spaces at the beginning
        $line =~ s/(\n*)|(\r\n*)//g;    # Substitute new lines at the end
        $cont++; 
        if ($line =~ m/.*(<$tag.*>)(.*)(<\/$tag>).*/sgi) {
            $line =~ s/^(\s*)//g;           # Substitute white spaces at the beginning
            $line =~ s/(\n*)|(\r\n*)//g;    # Substitute new lines at the end
            print "Tag found: $line, at line $cont\n";
            $matches++;
            $size--;
        } elsif($line =~ m/.*<$tag.*>.*/sgi) {
            my $tagLines = "";
            my $lines = "";
            $tagLines = $tagLines."Tag found: $line";
            $lines = $lines.", at lines $cont";
            
            $line = shift(@array);
            $line =~ s/^(\s*)//g;           # Substitute white spaces at the beginning
            $line =~ s/(\n*)|(\r\n*)//g;    # Substitute new lines at the end
            $cont++; $size--;
            
            while(($size>=0) and ($line !~ m/.*<\/$tag>.*/sgi)) {
                if ($size == 0){
                    $ec->setProperty("outcome","error");
                    print "Error! Unable to find closing tag, there is something wrong with the HTML code.\n";
                    return;
                }
                $tagLines = $tagLines."$line";
                $lines = $lines.",$cont";
                
                $line = shift(@array);
                $line =~ s/^(\s*)//g;           # Substitute white spaces at the beginning
                $line =~ s/(\n*)|(\r\n*)//g;    # Substitute new lines at the end
                $cont++; $size--;
            }
            $lines = $lines.",$cont.\n";
            $tagLines = $tagLines."$line".$lines;
            print $tagLines;
            $matches++;
            $size--;
        } else { $size--; }
    }
    # If no matches...
    if($matches eq 0){
      $ec->setProperty("outcome","warning");
      print "No matches found for the tag '<".$tag.">'\n";
    } else {
      print "Total matches: $matches\n";
    }
    return;
}


########################################################################
# rest_request - Makes an REST API call and prints the server's response
#
# Arguments:
#   -baseUrl        URL base
#   -pathUrl        Rest of the URL
#   -port           Server's Port
#   -contentType    Content Type
#   -formContent    Form Content
#   -username       
#   -password
#   -requestType    GET, POST, PUT or DELETE
#   -authentication No autehtication, or Basic
#   -headers        
#   -response_outpp Allows tht user to store the server's response on a EC property.
#
# Returns:
#   -nothing
#
########################################################################
sub rest_request {
    my ($baseUrl, $pathUrl,$port,$contentType,$formContent,$username,$password,$requestType,$authentication,$headers,$response_outpp) = @_;
    
    # Constants and Vars
    my @headers = split("\n",$headers);    
    my $whitespace = q{ };
    my $empty = q{};
    my $slash = q{/};
    my $url;
    my $req;
    my $response;
    my $browser = LWP::UserAgent->new;
	my $colon = q{:};
    my $urlText = $empty;
    
    # Validate Port
    if($port eq $empty){ 
        $urlText = $baseUrl.$slash.$pathUrl;
    } else {
        $urlText = $baseUrl.$colon.$port.$slash.$pathUrl;
    }
    
    # Validate URL
    if($urlText !~ m/^(https?:\/\/|)([A-Za-z0-9._-]+(:\d+)*)([\/?].*)?$/ixsmg) {
        $ec->setProperty("outcome","error");
        print "Invalid URL: ".$urlText."\n";
		print "Please check the provided URL, or contact support\n"; return;
    }
	
	# Request Method
    $url = URI->new($urlText);
    if ($requestType eq "POST") {
        $req = HTTP::Request->new(POST => $url);
    } elsif ($requestType eq "PUT") {
        $req = HTTP::Request->new(PUT => $url);
    } elsif ($requestType eq "DELETE") {
        $req = HTTP::Request->new(DELETE => $url);
    } else {
        $req = HTTP::Request->new(GET => $url);
    }
	
	# Create authorization to server
    if ($username ne $empty and $password ne $empty and $authentication eq "basic") {$req->authorization_basic($username, $password);} 

	# Set headers    
	my $size = @headers;
	if(($size>0) and ($headers[0] ne $empty)){
        foreach my $row (@headers){
            my ($key,$value) = split($whitespace,$row);        
		    if($key && $value){
                $req->header(qq{$key} => qq{$value});
            }
        }
    }
	
    # Set Request Content type
    if ($contentType ne $empty) { $req->content_type($contentType);}
   
    # Set Request Content
    if ($formContent ne $empty) { $req->content($formContent);}
	
	print "Creating Request...\n";
	# Print Request Parameters
    print "> Request:\n" . $req->as_string;

    # Perform Request
	print "Sending Request to Server...\n";
    $response = $browser->request($req);
	
    # Check for errors
	print "----------------------------------------------------------------------------------------------------\n";
	print "Response Received.\nChecking for errors...\n";
    if ($response->is_error) {
	    $ec->setProperty("outcome","error");
        
	    if ($response->status_line ne $empty) { print "\nStatus: ".$response->status_line."\n";}
        print ("ERROR! The server was unable to fulfil the request.\nPlease check your parameters.");
	    return;
    }
    print "Proceed to print response.\n";
    
    # Print Response
    print "> Response\n";	
    my @res = split("\n",$response->as_string);
	my $format=$empty;
	$size = @res;
    my $response_line = shift(@res);
    while(($size>1) and ($response_line !~ m/(^{).*|(^\[).*|(^<).*/ixsmg)) {
		if($response_line =~ m/Content-Type:\s(.*)\/([A-Za-z0-9-.]*)/ixsmg){$format=$2;}
		print $response_line."\n";
        $response_line = shift(@res);
        $size--;
	}
    my $response_body = $response->content();
    
	# Response Format Validation	
    if($format eq "xml"){
	    my $twig = XML::Twig->new( pretty_print => 'indented' );#'nsgmls', 'nice','indented' 'record'or 'record_c'
        $twig->parse($response_body);
        $twig->root->print;
    } elsif($format eq "json" or $format eq "javascript") {
	    my $json = JSON->new->allow_nonref;        
        my $perl_scalar = $json->decode( $response_body );        
		print $json->pretty->encode( $perl_scalar );
	} else{
	    print $response_body;
	}
    
    # Validate property storage    
    if($response_outpp ne $empty) { $ec->setProperty($response_outpp,$response_body); }    
    return;
}


sub plugin_info{
    # Print plugin Info
    my $pluginKey = 'EC-WebAccess';
    my $xpath = $ec->getPlugin($pluginKey);
    my $pluginName = $xpath->findvalue('//pluginVersion')->value;
    print "Using plugin $pluginKey version $pluginName";
    print "\n----------------------------------------------------------------------------------------------------\n";
}

1;
