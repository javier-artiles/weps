#!/usr/bin/perl

use strict;

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

my %no_reason = ("Not Found", 1, 
		 "Foreign Language", 1, 
		 "No Name", 1, 
		 "No Target Person", 1, 
		 "No Value", 1, 
		 "Multiple Target", 1, 
		 "Search Result", 1, 
		 "Fictional Character", 1, 
		 "Object Name", 1, 
		 );


# Otis_Lee:31 should be excluded bacause of the author's request
#---------------------------------------------------------------
my %exclude_data = ();
$exclude_data{"Otis_Lee"}{31}=1;

my $org_data_dir = "/home/sekine/PROJECT/WePS/weps2corpus_new/weps2corpus";
my $ae_data_dir = "../AE";
my $no_data_dir = "../NO";

my %org_data_id = ();
my %ae_data = ();
my %no_data = ();


########################
## MAIN PROGRAM
########################

# Check names in @WePS2_names
#----------------------------
check_name();

# Read AE/NO data
#-----------------------
read_ae_data();
read_no_data();
read_org_data_id();

delete_exclude_data();


# Check IDs
#-----------------------
check_ids();


########################
## Subroutine
########################
#---------------------------
#  Check if @WePS2_names matches the files in the org_data/*/files/*
#---------------------------
sub
check_name()
{
    my $n = 0;
    while(my $dir_name=<$org_data_dir/*/files/*>){
	$dir_name =~ m!/([^/]+)$!;
	my $name = $1;

	next if($name =~ /^[\s\.]+$/);

	print STDERR "check_name(): name=|$name|\n";

	my $flag = 0;
	for my $name0 ( @WePS2_names ){
	    $flag = 1 if($name eq uc( $name0 ));
	}
	die("Unmatch names name=|$name| in $org_data_dir") if($flag==0);
	$n++;
    }

    die("Number of names unmatch ($n<=>$#WePS2_names)") if($n!=$#WePS2_names+1);
}


#---------------------------
# Read AE data
#---------------------------
sub
read_ae_data( $ )
{
    foreach my $name0 ( @WePS2_names ){
	my $filename = "$ae_data_dir/$name0.txt";

	print STDERR "read_ae_data(): name0=|$name0|\n";

	my $linen = 0;
	open(FILE,$filename) or die("Can't open $filename");
	while(my $line = <FILE>){
	    $line =~ s/\r//g;
	    chomp($line);
	    $linen++;

	    if($line =~ /^(\d+)\t([^\t]+)\t(.*?)\s*$/){
		my ($id, $att, $val) = ($1, $2, $3);

		$att =~ s/\s+//g;
		$att =~ tr/[A-Z]/[a-z]/;
		unless(exists( $attribute{$att} )){
		    print "BUG: No attribute name=|$name0| $id=|$id| attribute=|$att|\n";
		}

		$id =~ s/^0+//;
		foreach my $v (split( /\t+/, $val)){
		    $ae_data{$name0}{$id}{$att}{$v} = 1;
		}
	    }elsif($line =~ /^\s*$/){
		next;
	    }else{
		print STDERR "FORMAT ERROR ($filename:$linen) : $line\n";
		exit(1);
	    }
	}
	close(FILE);
    }
}


#---------------------------
# Read NO data
#---------------------------
sub
read_no_data( $ )
{
    foreach my $name0 ( @WePS2_names ){
	my $filename = "$no_data_dir/NO_$name0.txt";

	print STDERR "read_no_data(): name0=|$name0|\n";

	my $linen = 0;
	open(FILE,$filename) or die("Can't open $filename");
	while(my $line = <FILE>){
	    chomp($line);
	    my ($id, $comment) = split( /\t/, $line );

	    $comment =~ s/\s+/ /g;
	    $comment =~ s/^ //;
	    $comment =~ s/ $//;

	    unless(exists( $no_reason{$comment} )){
		print "BUG: unknown no_reason name=|$name0| id=|$id| reason=|$comment|\n";
	    }

	    $no_data{$name0}{$id} = $comment;
	}
	close(FILE);
    }
}


#-----------------------------
# Read original data IDs
#-----------------------------
sub
read_org_data_id()
{
    foreach my $name0 ( @WePS2_names ){
	my $name0uc = uc($name0);
	my $count = 0;

	while(my $dir_name = <$org_data_dir/*/files/$name0uc>){
	    die("More than one file for $name0uc") if(++$count>1);

	    print STDERR "read_org_data_id() name0uc=|$name0uc|\n";

	    while(my $filename = <$dir_name/*>){

		$filename =~ m!/(.[^/]+)$!;
		my $filename0 = $1;

		unless($filename0 =~ m!(\d+).(html|txt)!){
		    die("Strange filename ($filename0)");
		}
		my $id = $1;
		$id =~ s/^0+//;

#		print "Reading org filename =|$filename| name0=|$name0| id=|$id|\n";

		$org_data_id{$name0}{$id} = 1;
	    }
	}
    }
}


#-----------------------------
# delete exclude data
#-----------------------------
sub
delete_exclude_data()
{
    foreach my $name (keys %exclude_data){
	foreach my $id (keys %{$exclude_data{$name}}){
	    delete( $org_data_id{$name}{$id} );
	    delete( $ae_data{$name}{$id} );
	    delete( $no_data{$name}{$id} );
	}
    }
}


#-----------------------------
# Check IDs
#-----------------------------
sub
check_ids()
{
    foreach my $name ( @WePS2_names ){

	print STDERR "check_ids() name=|$name|\n";

	foreach my $id (sort {$a <=> $b} keys %{$org_data_id{$name}} ){

	    unless(exists($ae_data{$name}{$id}) || 
		   exists($no_data{$name}{$id})){
		print("BUG: No AE/NO data for name=|$name| id=|$id|\n");
	    }
	}

	foreach my $id (sort {$a <=> $b} keys %{$ae_data{$name}} ){

	    unless(exists($org_data_id{$name}{$id})){
		print("BUG: No org data for name=|$name| id=|$id| in AE\n");
	    }

	    if(exists($no_data{$name}{$id})){
		print("BUG: Duplicated data for name=|$name| id=|$id| in AE and NO\n");
	    }
	}

	foreach my $id (sort {$a <=> $b} keys %{$no_data{$name}}){

	    unless(exists($org_data_id{$name}{$id})){
		print("BUG: No org data for name=|$name| id=|$id| in NO\n");
	    }
	}
    }
}
