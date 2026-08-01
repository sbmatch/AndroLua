require 'import'

import 'org.apache.commons.io.*'
import 'android.widget.*'
import 'android.os.*'
import 'android.app.*'
import 'android.net.*'
import 'android.view.*'
import 'android.util.*'
import 'android.provider.*'
import 'android.telephony.*'

context = activity:getApplicationContext()

local libDir = activity:getApplicationInfo().nativeLibraryDir
local dataDir = activity:getApplicationInfo().dataDir

filesDir = activity:getFilesDir():getAbsolutePath()
cacheDir = context:getCacheDir():getAbsolutePath()

local lua_version = _VERSION:match("(%d+%.%d+)")

package.cpath = string.format("%s;%s/lib?.so;%s/?.so;%s/usr/local/lib/lua/%s/?.so", package.cpath, libDir, libDir, filesDir, lua_version)

package.path = string.format("%s;%s/?.lua;%s/?/init.lua", package.path, filesDir, filesDir)


function copyAssetToDir(assetName, destFilePath)
      local inputStream = context:getAssets():open(assetName)
      local destFile = File(destFilePath .. "/" .. assetName)
      FileUtils:copyInputStreamToFile(inputStream, destFile)
end

copyAssetToDir("luasocket.zip", cacheDir)

local success = os.execute("unzip -o " .. cacheDir .. "/luasocket.zip" .. " -d " .. filesDir)
