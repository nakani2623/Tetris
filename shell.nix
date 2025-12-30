{ pkgs ? import <nixpkgs> {} }:

let
  libs = with pkgs; [
    gtk3 glib pango cairo gdk-pixbuf atk
    xorg.libX11 xorg.libXext xorg.libXrender xorg.libXtst xorg.libXi xorg.libXxf86vm
    mesa libGL
    fontconfig freetype
  ];
in
pkgs.mkShell {
  packages = with pkgs; [
    jdk21 gradle
    # you can keep openjfx here for its runtime deps even if you use Maven OpenJFX
    openjfx
  ] ++ libs;

  shellHook = ''
    export LD_LIBRARY_PATH="${pkgs.lib.makeLibraryPath libs}:$LD_LIBRARY_PATH"
  '';
}
