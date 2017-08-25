/*
 * Copyright 2017-2021 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package com.amazon.dtasdk.v2.serialization.messages;

import com.amazon.dtasdk.base.InstantAccessOperationValue;
import com.amazon.dtasdk.base.InstantAccessRequest;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Serializable GetUserId object to pass to developer
 */
public class GetUserIdSerializableRequest extends InstantAccessRequest {
    private String infoField1;
    private String infoField2;
    private String infoField3;

    @Override
    public GetUserIdSerializableRequest setOperation(InstantAccessOperationValue operation) {
        this.operation = operation;
        return this;
    }

    public String getInfoField1() {
        return infoField1;
    }

    public void setInfoField1(String infoField1) {
        this.infoField1 = infoField1;
    }

    public String getInfoField2() {
        return infoField2;
    }

    public void setInfoField2(String infoField2) {
        this.infoField2 = infoField2;
    }

    public String getInfoField3() {
        return infoField3;
    }

    public void setInfoField3(String infoField3) {
        this.infoField3 = infoField3;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((operation == null) ? 0 : operation.hashCode());
        result = prime * result + ((infoField1 == null) ? 0 : infoField1.hashCode());
        result = prime * result + ((infoField2 == null) ? 0 : infoField2.hashCode());
        result = prime * result + ((infoField3 == null) ? 0 : infoField3.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final GetUserIdSerializableRequest other = (GetUserIdSerializableRequest) obj;
        if (operation == null) {
            if (other.operation != null) {
                return false;
            }
        } else if (!operation.equals(other.operation)) {
            return false;
        }
        if (infoField1 == null) {
            if (other.infoField1 != null) {
                return false;
            }
        } else if (!infoField1.equals(other.infoField1)) {
            return false;
        }

        if (infoField2 == null) {
            if (other.infoField2 != null) {
                return false;
            }
        } else if (!infoField2.equals(other.infoField2)) {
            return false;
        }

        if (infoField3 == null) {
            if (other.infoField3 != null) {
                return false;
            }
        } else if (!infoField3.equals(other.infoField3)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("operation", operation)
                .append("infoField1", infoField1).append("infoField2", infoField2).append("infoField3", infoField3)
                .toString();
    }
}
