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
package com.amazon.dtasdk.serializer;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.junit.Ignore;

@Ignore
public class TestJson {
    private String testValue = "unset";

    public static class Name {
        private String first, last;

        public String getFirst() {
            return first;
        }

        public String getLast() {
            return last;
        }

        public void setFirst(String first) {
            this.first = first;
        }

        public void setLast(String last) {
            this.last = last;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((first == null) ? 0 : first.hashCode());
            result = prime * result + ((last == null) ? 0 : last.hashCode());
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
            Name other = (Name) obj;
            if (first == null) {
                if (other.first != null) {
                    return false;
                }
            } else if (!first.equals(other.first)) {
                return false;
            }
            if (last == null) {
                if (other.last != null) {
                    return false;
                }
            } else if (!last.equals(other.last)) {
                return false;
            }
            return true;
        }
    }

    private List<Name> names;
    private String value;

    public List<Name> getNames() {
        return names;
    }

    public void setNames(List<Name> names) {
        this.names = names;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @JsonIgnore
    public void setTestValue(String testValue) {
        this.testValue = testValue;
    }

    @JsonIgnore
    public String getTestValue() {
        return testValue;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((names == null) ? 0 : names.hashCode());
        result = prime * result + ((testValue == null) ? 0 : testValue.hashCode());
        result = prime * result + ((value == null) ? 0 : value.hashCode());
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
        TestJson other = (TestJson) obj;
        if (names == null) {
            if (other.names != null) {
                return false;
            }
        } else if (names.size() != other.names.size()) {
            return false;
        } else {
            for (int i = 0; i < names.size(); i++) {
                if (!names.get(i).equals(other.names.get(i))) {
                    return false;
                }
            }
        }
        if (testValue == null) {
            if (other.testValue != null) {
                return false;
            }
        } else if (!testValue.equals(other.testValue)) {
            return false;
        }
        if (value == null) {
            if (other.value != null) {
                return false;
            }
        } else if (!value.equals(other.value)) {
            return false;
        }
        return true;
    }
}
