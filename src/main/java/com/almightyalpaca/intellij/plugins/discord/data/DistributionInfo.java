/*
 * Copyright 2017 Aljoscha Grebe
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.almightyalpaca.intellij.plugins.discord.data;

import com.intellij.openapi.application.ApplicationInfo;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class DistributionInfo implements Serializable, Cloneable
{
    @NotNull
    private final String code;
    @NotNull
    private final String version;
    @NotNull
    private transient DistributionInfo.Type type;

    public DistributionInfo(@NotNull ApplicationInfo info)
    {
        this(info.getBuild().getProductCode(), info.getFullVersion());
    }

    public DistributionInfo(@NotNull String code, @NotNull String version)
    {
        this.code = code;
        this.type = DistributionInfo.Type.get(code);
        this.version = version;
    }

    @NotNull
    public String getCode()
    {
        return code;
    }

    @NotNull
    public DistributionInfo.Type getType()
    {
        return type;
    }

    @NotNull
    public String getVersion()
    {
        return version;
    }

    @NotNull
    public String getName()
    {
        return this.type == DistributionInfo.Type.UNKNOWN ? DistributionInfo.Type.UNKNOWN.getName() + " (" + getCode() + ")" : this.type.getName();
    }

    @NotNull
    public String getAssetName()
    {
        return this.type.getAssetName();
    }

    @Override
    public boolean equals(Object o)
    {
        return o instanceof DistributionInfo && type.equals(((DistributionInfo) o).type) && version.equals(((DistributionInfo) o).version);
    }

    @Override
    public String toString()
    {
        return "DistributionInfo{" + "code='" + code + '\'' + ", version='" + version + '\'' + '}';
    }

    private void writeObject(@NotNull ObjectOutputStream out) throws IOException
    {
        out.defaultWriteObject();
    }

    private void readObject(@NotNull ObjectInputStream in) throws IOException, ClassNotFoundException
    {
        in.defaultReadObject();

        this.type = DistributionInfo.Type.get(code);
    }

    @SuppressWarnings({"MethodDoesntCallSuperMethod", "CloneDoesntDeclareCloneNotSupportedException"})
    @Override
    protected DistributionInfo clone()
    {
        return this;
    }

    public enum Type
    {
        ANDROID_STUDIO("Android Studio", "android-studio", "AI"),
        APPCODE("AppCode", "appcode", "OC"),
        CLION("CLion", "clion", "CL"),
        DATAGRIP("DataGrip", "datagrip", "DB"),
        GOLAND("GoLand", "goland", "GO"),
        INTELLIJ_IDEA_COMMUNITY("IntelliJ IDEA Community", "intellij-idea", "IC"),
        INTELLIJ_IDEA_ULTIMATE("IntelliJ IDEA Ultimate", "intellij-idea", "IU"),
        MPS("MPS", "mps", "MPS"),
        PHPSTORM("PhpStorm", "phpstorm", "PS"),
        PYCHARM_COMMUNITY("PyCharm Community", "pycharm", "PC"),
        PYCHARM_EDU("PyCharm Edu", "pycharm-edu", "PE"),
        PYCHARM_PROFESSIONAL("PyCharm Professional", "pycharm", "PY"),
        RIDER("Rider", "rider", "RD"),
        RUBYMINE("RubyMine", "rubymine", "RM"),
        WEBSTORM("WebStorm", "webstorm", "WS"),

        UNKNOWN("Unknown Distribution", "unknown", "");

        @NotNull
        private static final Map<String, DistributionInfo.Type> MAP;

        static
        {
            MAP = new HashMap<>();

            for (DistributionInfo.Type distribution : DistributionInfo.Type.values())
                for (String code : distribution.getCodes())
                if (MAP.put(code, distribution) != null)
                    throw new ExceptionInInitializerError("Two distributions cannot have the same code");
        }

        @NotNull
        private final String name;
        @NotNull
        private final String assetName;
        @NotNull
        private final String[] codes;

        Type(@NotNull String name, @NotNull String assetName, @NotNull String... codes)
        {
            this.name = name;
            this.assetName = assetName;
            this.codes = codes;
        }

        public static DistributionInfo.Type get(@NotNull String code)
        {
            return MAP.getOrDefault(code, UNKNOWN);
        }

        @NotNull
        public String getName()
        {
            return name;
        }

        @NotNull
        public String[] getCodes()
        {
            return codes;
        }

        @NotNull
        public String getAssetName()
        {
            return assetName;
        }
    }
}
