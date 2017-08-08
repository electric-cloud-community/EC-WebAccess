@::gMatchers = (  
  {
    id =>          "AddErrors",
    pattern =>     q{.*Error!\s(.*)},
    action =>      q{
                        my $msg;
                        $msg= "ERROR: $1";
                        my $desc = ((defined $::gProperties{"summary"}) ? $::gProperties{"summary"} : '');
                        setProperty("/myJobStep/outcome", 'error');
                        $desc.=$msg;
                        setProperty("summary", $desc . "\n");                                               
                    },
  },  
  {
    id =>          "GetSearchType", 
    pattern =>     q{^Search\sType:\s(.*)},
    action =>      q{
                        my $msg;
                        $msg= "Search Type: $1";
                        my $desc = ((defined $::gProperties{"summary"}) ? $::gProperties{"summary"} : '');
                        $desc.=$msg;
                        setProperty("summary", $desc . "\n");                        
                    },
  },  
  {
    id =>          "GetSearchOption",
    pattern =>     q{.*Search\sOption:(.*)},    
    action =>      q{
                        my $msg;                            
                        $msg= "Search Option: $1";
                        my $desc = ((defined $::gProperties{"summary"}) ? $::gProperties{"summary"} : '');
                        $desc.=$msg;
                        setProperty("summary", $desc . "\n");                        
                    },
  },  
  {
    id =>          "CheckCredentials",
    pattern =>     q{.*Check\sCredentials.*},    
    action =>      q{
                        my $msg;                            
                        $msg= "Credentials Checked";
                        my $desc = ((defined $::gProperties{"summary"}) ? $::gProperties{"summary"} : '');
                        $desc.=$msg;
                        setProperty("summary", $desc . "\n");                        
                    },
  },  
  {
    id =>          "CheckUrl",
    pattern =>     q{.*Check\sURL.*},    
    action =>      q{
                        my $msg;                            
                        $msg= "URL Checked";
                        my $desc = ((defined $::gProperties{"summary"}) ? $::gProperties{"summary"} : '');
                        $desc.=$msg;
                        setProperty("summary", $desc . "\n");                        
                    },
  },  
  {
    id =>          "GetHtml",
    pattern =>     q{.*Get\sHTML.*},    
    action =>      q{
                        my $msg;                            
                        $msg= "HTML Obtained";
                        my $desc = ((defined $::gProperties{"summary"}) ? $::gProperties{"summary"} : '');
                        $desc.=$msg;
                        setProperty("summary", $desc . "\n");                        
                    },
  },  
  {
    id =>          "TimeElapsed",
    pattern =>     q{.*Time\selapsed:(.*)},    
    action =>      q{
                        my $msg;                            
                        $msg= "Time elapsed: $1";
                        my $desc = ((defined $::gProperties{"summary"}) ? $::gProperties{"summary"} : '');
                        $desc.=$msg;
                        setProperty("summary", $desc . "\n");                        
                    },
  },
  
  {
   id =>        "StandardError",
   pattern =>          q{\((.*)(Error)\)},
   action =>           q{&addSimpleError("StandardError", "$1 $2");setProperty("outcome", "error" );updateSummary();},
  },  
  {
        id =>          "RequestType",
        pattern =>     q{^(POST|DELETE|PUT|GET)},
        action =>           q{
                              my $desc = ((defined $::gProperties{"summary"}) ? $::gProperties{"summary"} : '');
                              $desc .= "Request Type: $1";
                              setProperty("summary", $desc . "\n");
                             },
  },
  {
        id =>          "Accept",
        pattern =>     q{Accept:\s(.*)},
        action =>           q{
                              my $desc = ((defined $::gProperties{"summary"}) ? $::gProperties{"summary"} : '');
                              $desc .= "Accept: $1";
                              setProperty("summary", $desc . "\n");
                             },
  },  
  {
        id =>          "Status",
        pattern =>     q{^Status:\s(.*)},
        action =>           q{
                              my $desc = ((defined $::gProperties{"summary"}) ? $::gProperties{"summary"} : '');
                              $desc .= "HTTP Status Code: $1";
                              setProperty("summary", $desc . "\n");
                             },
  },
  {
        id =>          "InvalidURL",
        pattern =>     q{Invalid URL:\s(.*)},
        action =>           q{
                              my $desc = ((defined $::gProperties{"summary"}) ? $::gProperties{"summary"} : '');
                              $desc .= "Invalid URL: $1";
                              setProperty("summary", $desc . "\n");
                             },
  },
);

