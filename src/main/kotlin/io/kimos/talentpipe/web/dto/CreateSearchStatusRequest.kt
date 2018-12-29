package io.kimos.talentpipe.web.dto

import org.jetbrains.annotations.NotNull
import javax.validation.constraints.NotEmpty

class CreateSearchStatusRequest {
    @NotNull
    @NotEmpty
    var name:String = ""
    @NotNull
    @NotEmpty
    var description:String = ""
}
