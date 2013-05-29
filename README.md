XMLParse
========
XML Downloader Version 3. 
Author Roy Hotrabhvanon
//////
code repository located at
www.github.com/royh/xmlparse
//////


Instructions for Use.

The only file you need to worry about is the Config file this needs to be located in the same directory as the program. (config.txt)

the config file is where you add the Streams to be downloaded.
NOTE: EACH NEW LINE IS A NEW STREAM!!

this is basic structure of each line.

NAMEofSTREAM,XMLLINK,ALBUMTAG,SAVEDIRECTORY

Name of Stream : This is just an ID name its not used for anything but is required.
XMLLINK: This is the link to the xml file. Note some website have feeds that dont end in .xml this does not matter!! 

ALBUMTAG: What you want the ID3 tag to read (can contain spaces)

SaveDirectory: Where you want the Mp3 files to be saved this can be a full Directory path ie. (C:/documents/stuff/) or a of the type /audio (where / is the current working directory, ie location of the program).


Important Notes.
- You must have a different Save directory for each stream
- The Save directory Must exist, this program will crash if it cant find the folder to save it to.


EXAMPLE of a Config file

Stream 1 
Name : NYTBOOKS
XML LINK:http://www.nytimes.com/services/xml/rss/nyt/podcasts/bookupdate.xml
Tag : NYT Books
Save Dir : audio/

This is what is should look like in the config file

NYTBOOKS,http://www.nytimes.com/services/xml/rss/nyt/podcasts/bookupdate.xml,NYT Books,audio/


Stream 2
Name :FSN News
XML Link : http://fsrn.org/taxonomy/term/5/0/feed
Tag : FSN News
SaveDir : FSN News/

FSN News,http://fsrn.org/taxonomy/term/5/0/feed,FSN News,FSN NEWS/



