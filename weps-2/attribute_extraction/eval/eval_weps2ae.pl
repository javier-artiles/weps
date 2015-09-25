#!/usr/bin/perl

use strict;


####################
## WePS2AE SETTINGS
####################
my @WePS2_names = ("Benjamin_Snyder",  "Cheng_Niu",       "David_Weir", 
		   "Emily_Bender",     "Gideon_Mann",     "Hao_Zhang", 
		   "Hui_Fang",         "Ivan_Titov",      "Mirella_Lapata", 
		   "Tamer_Elsayed",    "Amanda_Lentz",    "Helen_Thomas", 
		   "Janelle_Lee",      "Jonathan_Shaw",   "Judith_Schwartz", 
		   "Otis_Lee",         "Rita_Fisher",     "Sharon_Cummings", 
		   "Susan_Jones",      "Theodore_Smith",  "Bertram_Brooker", 
		   "David_Tua",        "Franz_Masereel",  "Herb_Ritts", 
		   "James_Patterson",  "Jason_Hart",      "Louis_Lowe", 
		   "Mike_Robertson",   "Nicholas_Maw",    "Tom_Linton");

my %attribute = ( "dateofbirth", 1,      "birthplace", 1,  
		  "othername", 1,        "occupation", 1,  
		  "affiliation", 1,      "work", 0,  
		  "award", 1,            "school", 1,  
		  "major", 1,            "degree", 1,  
		  "mentor", 1,           "location", 0,  
		  "nationality", 1,      "relatives", 1,  
		  "phone", 1,            "fax", 1,  
		  "email", 1,            "website", 1  );

my $top_url = "http://apple.cs.nyu.edu/WePS2AE/data";


#####################
## GLOBAL VARIABLES
#####################

# ID information
#-------------------------------
my %id_list = ();

# Information about no-annotated data
#   $nodata{$id} = (0:no-value|1:others, $comment)
#-------------------------------
my %nodata = ();  

# Informatin about att-val data (data1=gold, data2=system)
#    @{$data1{$id}{$att}{$CleanUp-edValue}}= (n, $value);
#       n=1: retrived from data
#       n=2: used in matching
#-------------------------------
my %data1 = ();
my %data2 = ();

# Counting the matching result for all names
#    $total_count{$name}{$attr}{'MATCH/OVG/MISS/ALL'}
#    $total_all{'MATCH/OVG/MISS/ALL'}
#    $total_att_count{$att}{'MATCH/OVG/MISS/ALL'}
#    $total_name_count{$name}{'MATCH/OVG/MISS/ALL'}
#-------------------------------
my %total_count = ();
my %total_all_count = ();
my %total_attr_count = ();
my %total_name_count = ();

# Counting the matching result for each name
#    $count{'MATCH/OVG/MISS'}
#-------------------------------
my %count = ();

# Detail of the maching information
#    $result{$id}{$attr}{'MATCH/OVG/MISS'}{$val} = CleanUp-edValue
#-------------------------------
my %result = ();


##################
## PROGRAM
##################

unless($#ARGV==2){
    print STDERR "Usage: eval_weps2ae.pl Gold_dir System_dir Out_dir\n";
    print STDERR "\n";
    print STDERR "  Each dir indicates data directory containing WePS2AE\n";
    print STDERR "  data for Gold, System and Output of the result.\n";
    print STDERR "  The output files of this program are stored in Out_dir\n";
    print STDERR "  in html format. Open browser for Out_dir/result.html.\n";
    exit(1);
}


my ($dir_gold, $dir_sys, $dir_out) = ($ARGV[0], $ARGV[1], $ARGV[2]);
$dir_gold =~ s!/$!!;
$dir_sys =~ s!/$!!;
$dir_out =~ s!/$!!;
`mkdir $dir_out` unless(-e $dir_out);

my $name;

foreach my $n ( @WePS2_names ){

    $name = $n;

    init_each();

    readnofile("$dir_gold/NO/NO_$name.txt");

    %data1 = readfile("$dir_gold/AE", $name);
    %data2 = readfile("$dir_sys", $name);
    
    match();

    print_each();
}

calc_total_f();

print_total();


##################
## SUBROUTINE
##################

######################
## Initialize for each name
######################
sub
init_each()
{
    %nodata = ();  

    %data1 = ();
    %data2 = ();

    %id_list = ();

    %count = ();
    %result = ();
}


############################
## Read NO information
############################
sub
readnofile( $ )
{
    my $filename = shift;

    open(FILE,$filename) or die("Can't open $filename");
    while(my $line = <FILE>){
	chomp($line);
	my ($id, $comment) = split( /\t/, $line );

	$id_list{$id} = 1;
	if($comment eq "No Value"){
	    @{$nodata{$id}} = (0, "$comment");
	}else{
	    @{$nodata{$id}} = (1, $comment);
	}
    }
    close(FILE);
}


############################
## Read all data from a file
############################
sub
readfile( $ )
{
    my ($dir, $n) = @_;
    my %data = ();
    my $lines = "";

    my $filename = "$dir/$n.txt";
    my $uc_n = uc( $n );
    my $uc_filename = "$dir/$uc_n.txt";

    $filename = $uc_filename if(!(-e $filename) && (-e $uc_filename));
	
    open(FILE,$filename) or die("Can't open $filename");
    while(my $line = <FILE>){
	if($line =~ /^(\d+)\t([^\t]+)\t(.*?)\s*$/){
	    $lines .= $line;
	}elsif($line =~ /^\s*$/){
	    next;
	}else{
	    $lines =~ s/\n$/ /;
	    $lines .= $line;
	}
    }
    close(FILE);

    foreach my $line ( split( /\n/, $lines) ){
	$line =~ s/\r//g;
	chomp($line);

	if($line =~ /^(\d+)\t([^\t]+)\t(.*?)\s*$/){
	    my ($id, $att, $val) = ($1, $2, $3);
	    $id =~ s/^0+//;
	    $id_list{$id}++;
	    $att =~ s/\s+//g;
	    $att =~ tr/[A-Z]/[a-z]/;
	    foreach my $v (split( /\t+/, $val)){
		my $c_v = cleanup($v);
		@{$data{$id}{$att}{$c_v}}= (1, $v);
	    }
	}
    }

    return( %data );
}


##########################
## Clean up the data 
##  (minor differences are allowed)
##########################
sub
cleanup()
{
    my $val = shift;

    $val =~ s/\s+/ /;
    $val =~ s/^\s*//;
    $val =~ s/\s*$//;
    $val =~ s/^"//;
    $val =~ s/"$//;
    $val =~ s/\bthe //;
    $val =~ s/\bThe //;
    $val =~ s/\.$//;
    $val =~ s/,//;
    $val =~ s/^\s*//;
    $val =~ s/\s*$//;
    $val =~ s/\s+/ /;

    return($val);
}


############################
## Print out each into html
############################
sub
print_each()
{
    my $outfile = "$dir_out/$name.html";
    open(OUT, "> $outfile") or die("Can't open $outfile");

    print OUT "<head><title>";
    print OUT "WePS2AE matching result ($name)";
    print OUT "</title></head>\n";

    print OUT "<body>\n";

    print OUT "<h1>WePS2AE matching Result for $name</h1>\n";
    print OUT "<hr>\n";


    # Print match, ovg, miss, precision, recall and F-measure
    #-----------------------------------------------------------
    print OUT "<table border><tr>";
    foreach my $item ('MATCH', 'OVG', 'MISS'){
	print OUT "<td>$item</td>";
    }
    print OUT "<td>Precision</td><td>Recall</td><td>F-measure</td>";
    print OUT "</tr><tr>";
    foreach my $item ('MATCH', 'OVG', 'MISS'){
	print OUT "<td>$count{$item}</td>";
    }
    my $precision = ($count{'MATCH'}+$count{'OVG'}>0 ? 
		     100.0 * $count{'MATCH'}/($count{'MATCH'}+$count{'OVG'}) :
		     0);
    my $recall    = ($count{'MATCH'}+$count{'MISS'}>0 ?
		     100.0 * $count{'MATCH'}/($count{'MATCH'}+$count{'MISS'}) :
		     0);
    my $fmes      = ($precision + $recall>0 ?
		     2 * $precision * $recall / ($precision + $recall) :
		     0);
    printf OUT "<td>%7.3f</td><td>%7.3f</td><td>%7.3f</td>",
           $precision, $recall, $fmes;
    print OUT "</tr></table>\n";
    
    print OUT "<hr>\n";

    # Print analysis table
    #------------------------
    print OUT "<ul>";

    foreach my $id (sort {$a <=> $b} keys %id_list){
	my $url = sprintf "$top_url/%s/%03d.html",
	          $name, $id;
	print OUT "<li> <a href=\"$url\" target=\"_blank\">$id</a>\n";

	if(exists($nodata{$id})){
	    print OUT ": NO-annotate : $nodata{$id}[1]<br><p>";
	}

	if(!exists($nodata{$id}) || $nodata{$id}[0]==0){
	    print OUT "<p><table border><tr>";
	    print OUT "<td width=\%10>Attribute</td>";
	    print OUT "<td width=\%30>MATCH</td>";
	    print OUT "<td width=\%30>Over-generate</td>";
	    print OUT "<td width=\%30>Miss</td>";
	    print OUT "</tr>\n";

	    foreach my $attr (sort {$a cmp $b} keys %{$result{$id}}){
		print OUT "<tr><td>$attr</td>";
		foreach my $item ('MATCH', 'OVG', 'MISS'){
		    print OUT "<td><ul>";
		    foreach my $v (sort {$a cmp $b} keys %{$result{$id}{$attr}{$item}}){
			print OUT "<li>$result{$id}{$attr}{$item}{$v}";
		    }
		    print OUT "</ul></td>\n";
		}
		print OUT "</tr>";
	    }
	    print OUT "</table><p>\n";
	}

	print OUT "<u>Unused Information</u><br>\n";
	print OUT "<ul>";
	foreach my $attr (keys %{$data1{$id}}){
	    foreach my $val (keys %{$data1{$id}{$attr}}){
		if($attribute{$attr}==1 && ${$data1{$id}{$attr}{$val}}[0] != 2){
		    print OUT "<li>Gold: $attr $val\n";
	        }
	    }
	}
	foreach my $attr (keys %{$data2{$id}}){
	    foreach my $val (keys %{$data2{$id}{$attr}}){
		if($attribute{$attr}==1 && ${$data2{$id}{$attr}{$val}}[0] != 2){
		    print OUT "<li>System: $attr $val\n";
		}
	    }
	}
	print OUT "</ul>\n";
	print OUT "<p><hr>\n";
    }

    print OUT "</body></html>\n";
}


#########################
## Check the match
#########################
sub
match()
{
    foreach my $id (sort {$a <=> $b} keys %id_list){

	next if(exists($nodata{$id}) && $nodata{$id}[0]==1);

	foreach my $attr (keys %{$data1{$id}}){
	    if($attribute{$attr}==1){
		foreach my $val ( keys %{$data1{$id}{$attr}} ){
		    if( exists( $data2{$id}{$attr}{$val} )){
			my $v1 = ${$data1{$id}{$attr}{$val}}[1];
			my $v2 = ${$data2{$id}{$attr}{$val}}[1];
			if($v1 eq $v2){
			    $result{$id}{$attr}{'MATCH'}{$val}=$v1;
			}else{
			    $result{$id}{$attr}{'MATCH'}{$val}="|$v1|/|$v2|";
			}
			${$data1{$id}{$attr}{$val}}[0]=2;
			${$data2{$id}{$attr}{$val}}[0]=2;
			$count{'MATCH'}++;
	                $total_count{$name}{$attr}{'MATCH'}++;
	                $total_all_count{'MATCH'}++;
 	                $total_attr_count{$attr}{'MATCH'}++;
 	                $total_attr_count{$attr}{'ALL'}++;
 	                $total_name_count{$name}{'MATCH'}++;
 	                $total_name_count{$name}{'ALL'}++;
		    }else{
			$result{$id}{$attr}{'MISS'}{$val}=${$data1{$id}{$attr}{$val}}[1];
			${$data1{$id}{$attr}{$val}}[0]=2;
			$count{'MISS'}++;
	                $total_count{$name}{$attr}{'MISS'}++;
	                $total_all_count{'MISS'}++;
 	                $total_attr_count{$attr}{'MISS'}++;
 	                $total_attr_count{$attr}{'ALL'}++;
 	                $total_name_count{$name}{'MISS'}++;
 	                $total_name_count{$name}{'ALL'}++;
		    }
		}
	    }
	}

	foreach my $attr (keys %{$data2{$id}}){
	    if($attribute{$attr}==1){
		foreach my $val ( keys %{$data2{$id}{$attr}} ){
		    if( exists( $data1{$id}{$attr}{$val} )){
			# Dont printout
		    }else{
			$result{$id}{$attr}{'OVG'}{$val}=${$data2{$id}{$attr}{$val}}[1];
			${$data2{$id}{$attr}{$val}}[0]=2;
			$count{'OVG'}++;
	                $total_count{$name}{$attr}{'OVG'}++;
	                $total_all_count{'OVG'}++;
 	                $total_attr_count{$attr}{'OVG'}++;
 	                $total_attr_count{$attr}{'ALL'}++;
 	                $total_name_count{$name}{'OVG'}++;
 	                $total_name_count{$name}{'ALL'}++;
		    }
		}
	    }
	}
    }
}



############################
## Print out all into html
############################
sub
calc_total_f()
{
    foreach my $attr (keys %attribute){
	my $precision = $total_attr_count{$attr}{'PRECISION'} = 
	    ($total_attr_count{$attr}{'MATCH'}+$total_attr_count{$attr}{'OVG'}>0 ?
	     $total_attr_count{$attr}{'MATCH'}/
	     ($total_attr_count{$attr}{'MATCH'}+$total_attr_count{$attr}{'OVG'}) :
	     0);

	my $recall = $total_attr_count{$attr}{'RECALL'} = 
	    ($total_attr_count{$attr}{'MATCH'}+$total_attr_count{$attr}{'MISS'}>0 ?
	     $total_attr_count{$attr}{'MATCH'}/
	     ($total_attr_count{$attr}{'MATCH'}+$total_attr_count{$attr}{'MISS'}) :
	     0);

	$total_attr_count{$attr}{'F-MEASURE'} = 
	    ($precision+$recall ?
	     2*$precision*$recall/($precision+$recall) :
	     0);
    }


    foreach my $n ( @WePS2_names){
	my $precision = $total_name_count{$n}{'PRECISION'} = 
	    ($total_name_count{$n}{'MATCH'}+$total_name_count{$n}{'OVG'} ?
	     100.0 * $total_name_count{$n}{'MATCH'}/
	     ($total_name_count{$n}{'MATCH'}+$total_name_count{$n}{'OVG'}) :
	     0);
	my $recall = $total_name_count{$n}{'RECALL'} = 
	    ($total_name_count{$n}{'MATCH'}+$total_name_count{$n}{'MISS'}>0 ?
	     100.0 * $total_name_count{$n}{'MATCH'}/
	     ($total_name_count{$n}{'MATCH'}+$total_name_count{$n}{'MISS'}) :
	     0);
	$total_name_count{$n}{'F-MEASURE'} = 
	    ($precision + $recall>0 ?
	     2 * $precision * $recall / ($precision + $recall) :
	     0);
    }
}

    

sub
print_total()
{
    my $outfile = "$dir_out/result.html";
    open(OUT, "> $outfile") or die("Can't open $outfile");

    print OUT "<head><title>";
    print OUT "WePS2AE matching TOTAL result ($dir_sys)";
    print OUT "</title></head>\n";

    print OUT "<body>\n";

    print OUT "<h1>WePS2AE matching TOTAL Result ($dir_sys)</h1>\n";
    print OUT "<hr>\n";
    my $precision = ($total_all_count{'MATCH'}+$total_all_count{'OVG'}>0 ?
		     100.0 * $total_all_count{'MATCH'}/
		     ($total_all_count{'MATCH'}+$total_all_count{'OVG'}) :
		     0);
    my $recall = ($total_all_count{'MATCH'}+$total_all_count{'MISS'}>0 ?
		  100.0 * $total_all_count{'MATCH'}/
		  ($total_all_count{'MATCH'}+$total_all_count{'MISS'}) :
		  0);
    my $fmes = ($precision + $recall>0 ?
		2 * $precision * $recall / ($precision + $recall) :
		0);

    print OUT "<nl>\n";
    print OUT "<li>Match = $total_all_count{'MATCH'}<br>\n";
    print OUT "<li>Over-generate = $total_all_count{'OVG'}<br>\n";
    print OUT "<li>Miss = $total_all_count{'MISS'}<br>\n";
    printf OUT "<li>Precision = %7.3f<br>\n", $precision;
    printf OUT "<li>Recall = %7.3f<br>\n", $recall;
    printf OUT "<li>F-measure = %7.3f<br>\n", $fmes;
    print OUT "</nl>\n";

    printf "%s:\tprec=%7.3f\trecall=%7.3f\tf=%7.3f\n",$dir_sys,$precision,$recall,$fmes;

    print OUT "<hr>\n";

    # Print match, ovg, miss, precision, recall and F-measure
    #-----------------------------------------------------------

    # Header line
    #------------
    print OUT "<table border><tr>";
    print OUT "<td>Name</td>";
    print OUT "<td>Match</td><td>Over-generate</td><td>Miss</td>";
    print OUT "<td>Precision</td><td>Recall</td><td>F-Measure</td>";
    foreach my $att (sort {$a cmp $b} keys %attribute){
	print OUT "<td>$att</td>";
    }
    print OUT "</tr>\n";

    # Total line
    #--------------------
    foreach my $item ('MATCH', 'OVG', 'MISS', 'PRECISION', 'RECALL', 'F-MEASURE'){
	print OUT "<tr><td>TOTAL ($item)</td>";
	print OUT "<td></td><td></td><td></td><td></td><td></td><td></td>";

	foreach my $att (sort {$a cmp $b} keys %attribute){
	    if($item eq 'MATCH' || $item eq 'OVG' || $item eq 'MISS'){
		print OUT "<td>$total_attr_count{$att}{$item}</td>";
	    }else{
		printf OUT "<td>%7.3f</td>",$total_attr_count{$att}{$item};
	    }
	}
	print OUT "</tr>\n";
    }

    foreach my $name ( @WePS2_names ){

	print OUT "<tr><td><a href=\"$name.html\">$name</a></td>";

	foreach my $item ('MATCH', 'OVG', 'MISS', 'PRECISION', 'RECALL', 'F-MEASURE'){
	    if($item eq 'MATCH' || $item eq 'OVG' || $item eq 'MISS'){
		print OUT "<td>$total_name_count{$name}{$item}</td>";
	    }else{
		printf OUT "<td>%7.3f</td>",$total_name_count{$name}{$item};
	    }
	}

	foreach my $attr (sort {$a cmp $b} keys %attribute){

	    print OUT "<td>";
	    print OUT "$total_count{$name}{$attr}{'MATCH'} / ";
	    print OUT "$total_count{$name}{$attr}{'OVG'} : ";
	    print OUT "$total_count{$name}{$attr}{'MISS'}";
	    print OUT "</td>";
	}
    }
}
