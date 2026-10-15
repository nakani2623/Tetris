#ifndef SDL_MANAGER_H
#define SDL_MANAGER_H

#include <SDL3/SDL.h>
class Sdl_manager
{
private:
    /* data */
    SDL_Window *create_window(int width, int height);

    SDL_Window *window;
    SDL_Renderer *renderer;
public:
    Sdl_manager(int width, int height);
    void simple_window();
    ~Sdl_manager();
};
// SDL_Window *Sdl_manager::create_window(int width, int height) {
//     SDL_CreateWindow(
//         "SDL window",
//         width, height, 0
//     );
// }
#endif


