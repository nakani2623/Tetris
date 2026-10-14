g++ -shared -fPIC     app/native/sdl_renderer.cpp     -o app/libsdl_renderer.so     $(pkg-config --cflags --libs sdl3)
gradle run