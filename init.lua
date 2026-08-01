require 'import'
context = activity:getApplicationContext()

local libDir = activity:getApplicationInfo().nativeLibraryDir
local dataDir = activity:getApplicationInfo().dataDir
local filesDir = activity:getFilesDir():getAbsolutePath()

package.cpath = string.format("%s;%s/lib?.so;%s/?.so", package.cpath, libDir, libDir)

package.path = string.format("%s;%s/?.lua;%s/?/init.lua", package.path, filesDir, filesDir)