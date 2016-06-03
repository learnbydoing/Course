1)
EXEC member_registration(2009, 'Adams', 'adams_1@yahoo.com', '3123621111', '02-SEP-2013', '01-SEP-2018')
2)
EXEC update_expiration_date(90, '01-SEP-20');
EXEC update_expiration_date(2009, '01-SEP-20');

3)
EXEC video_search('ocean');
EXEC video_search('PRETTY WOMAN', 'Blu-Ray');
EXEC video_search('Pretty Woman');
EXEC video_search('Another');
EXEC video_search('ANOTHER', 'BLU');
EXEC video_search('Story');
EXEC video_search('Story', 'TAPE');

4)
EXEC video_checkout(330, 6001, '18-APR-14');
EXEC video_checkout(2009, 6015, '18-APR-16');

5)
EXEC video_return(123, '18-APR-16');
EXEC video_return(6010, '18-APR-17');
EXEC video_return(6013, '18-APR-17');
EXEC video_return(6020, '01-MAY-16');

6)
EXEC print_unreturned_video(90);
EXEC print_unreturned_video(2004);
EXEC print_unreturned_video(2008);
EXEC print_unreturned_video(2002);
EXEC print_unreturned_video(2003);
