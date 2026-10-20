package com.kennypvthax.uisuite;

/** Host integration boundary. No file deletion exists without a specified authorized target. */
interface GameActions {
 enum Availability { INSTALLED,NOT_INSTALLED,UNKNOWN }
 final class Status {
  final Availability availability;final boolean launchable;
  Status(Availability availability,boolean launchable){this.availability=availability;this.launchable=launchable;}
 }
 Status inspect(String packageName);
 boolean launch(String packageName);
 boolean fileActionsAvailable();
}
