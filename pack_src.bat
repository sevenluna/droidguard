rem @echo off
SET PACK_NAME=droidguard%1_src.zip
SET X_LISTFILE=src_x_filelist.txt
echo Packing...
7z a -tzip %PACK_NAME% -xr@%X_LISTFILE%
echo Done!
pause
