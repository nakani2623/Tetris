{ pkgs ? import (builtins.fetchTarball {
        url = "https://github.com/NixOS/nixpkgs/archive/2074cc672349f14240fb7ea90d509e0b643e9dd4.tar.gz";
    sha256 = "0x2cbp7qi17ll07dlns0bq6401r6563bvkv3bqvd6lia5ai5hj0k";
  }) {} }:

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
    jdk21 gradle_9
    # you can keep openjfx here for its runtime deps even if you use Maven OpenJFX
    openjfx
  ] ++ libs;

  shellHook = ''
    export LD_LIBRARY_PATH="${pkgs.lib.makeLibraryPath libs}:$LD_LIBRARY_PATH"
  '';
  
}

